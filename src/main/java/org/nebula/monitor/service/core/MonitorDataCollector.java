/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nebula.monitor.service.core;

import org.apache.log4j.Logger;
import org.nebula.admin.client.model.monitor.MonitorConfigSummary;
import org.nebula.admin.client.model.monitor.MonitorData;
import org.nebula.monitor.service.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.JedisPool;

public class MonitorDataCollector {

  public final static int MAX_MONITOR_DATA_NUMBER = 100;

  private final static Logger logger = Logger
      .getLogger(MonitorDataCollector.class);

  @Autowired
  private JedisPool jedisPool;

//	@Autowired
//	private NebulaMgmtClient nebulaMgmtClient;

  public void collect() {

    new Thread(new SeedCollector()).start();

  }

  private class SeedCollector implements Runnable {

    private ThreadPoolExecutor executor;

    public SeedCollector() {
      executor = new ThreadPoolExecutor(10, 50, 10, TimeUnit.MINUTES,
                                        new ArrayBlockingQueue(10),
                                        new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public void run() {
      MonitorQueue monitorConfigQueue = new MonitorQueue(jedisPool,
                                                         SeedGenerator.MONITOR_CONFIG_QUEUE);
      while (true) {
        try {

          String data = monitorConfigQueue.get(10);

          if (data != null) {
            logger.info("DATA=" + data);
            executor.execute(new DataCollector(
                (MonitorConfigSummary) JsonUtils.toObject(data,
                                                          MonitorConfigSummary.class)));
          }

        } catch (Exception e) {
          logger.error("Failed to get monitor config", e);
        }
      }
    }
  }

  private class DataCollector implements Runnable {

    private MonitorConfigSummary monitorConfigSummary;

    public DataCollector(MonitorConfigSummary monitorConfigSummary) {
      this.monitorConfigSummary = monitorConfigSummary;
    }

    public void run() {

//			GetRunningWorkflowRequest request = new GetRunningWorkflowRequest();
//
//			long backlogPeriodDate = System.currentTimeMillis()
//					- monitorConfigSummary.getDuration() * 60 * 1000;
//
//			List<String> registrationIds = new ArrayList<String>();
//			registrationIds.add(monitorConfigSummary.getMonitorId());
//
//			request.setBacklogPeriodDate(backlogPeriodDate);
//			request.setRegistrationIds(registrationIds);
//
//			GetRunningWorkflowResponse response = null;
//			try {
////				response = nebulaMgmtClient.getRunningWorkflow(request);
//
//				int count = response.getCountNum();
//				logger.info("COUNT=" + count);
//
//				MonitorQueue monitorDataQueue = new MonitorQueue(jedisPool,
//						new MonitorDataQueueName(monitorConfigSummary.getId())
//								.toString());
//
//				boolean alarm = false;
//				int threshold = monitorConfigSummary.getUncompletedNumber();
//				if (count > threshold
//						&& exceedThresholdForTimes(monitorDataQueue, threshold,
//								monitorConfigSummary.getConsecutiveTimes() - 1)) {
//					alarm = true;
//					logger.warn("Send email to "
//							+ monitorConfigSummary.getEmails());
//				}
//
//				putToMonitorQueue(monitorDataQueue,
//						monitorConfigSummary.getId(), count, alarm);
//			}
//			catch (Exception e) {
//				logger.error("Failed to get Running Workflow", e);
//			}

    }

    private boolean exceedThresholdForTimes(MonitorQueue monitorDataQueue,
                                            int threshold, int times) {
      List<String> consecutiveData = monitorDataQueue.range(0, times - 1);

      for (String data : consecutiveData) {
        MonitorData monitorData = (MonitorData) JsonUtils.toObject(
            data, MonitorData.class);
        if (monitorData.getCount() <= threshold) {
          return false;
        }
      }
      return true;
    }

    private void putToMonitorQueue(MonitorQueue monitorDataQueue,
                                   String configId, int count, boolean alarm) {

      MonitorData monitorData = new MonitorData();
      monitorData.setConfigId(configId);
      monitorData.setCount(count);
      monitorData.setAlarm(alarm);
      monitorData.setCreatedDate(new Date(System.currentTimeMillis()));

      monitorDataQueue.trim(0, MAX_MONITOR_DATA_NUMBER - 2);

      monitorDataQueue.put(JsonUtils.toJson(monitorData));
    }
  }

}
