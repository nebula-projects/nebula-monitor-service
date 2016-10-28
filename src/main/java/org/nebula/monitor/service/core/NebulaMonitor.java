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
import org.nebula.framework.activity.ActivityWorker;
import org.nebula.framework.client.NebulaClient;
import org.nebula.framework.client.NebulaRestClient;
import org.nebula.framework.core.Configuration;
import org.nebula.framework.workflow.WorkflowWorker;
import org.nebula.monitor.service.dao.mapper.StatisticMapper;
import org.nebula.monitor.service.worker.GetDomainsActivityImpl;
import org.nebula.monitor.service.worker.OneMinuteWorker;
import org.nebula.monitor.service.worker.OneMinuteWorkerImpl;
import org.nebula.monitor.service.worker.SaveStatisticActivityImpl;
import org.nebula.monitor.service.worker.StatisticActivityImpl;
import org.nebula.monitor.service.worker.StatisticWorker;
import org.nebula.monitor.service.worker.StatisticWorkerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class NebulaMonitor implements Runnable {

  private final static Logger logger = Logger.getLogger(NebulaMonitor.class);
  @Autowired
  StatisticMapper statisticMapper;
  @Value("${nebula.service.username}")
  String nebulaServiceUsername;
  @Value("${nebula.service.password}")
  String nebulaServicePassword;
  @Value("${nebula.service.host}")
  String nebulaServiceHost;
  @Value("${nebula.service.port}")
  int nebulaServicePort;
  @Value("${nebula.service.contextPath}")
  String nebulaServiceContextPath;
  private NebulaClient nebulaClient;
  private ActivityWorker activityWorker;
  private WorkflowWorker workflowWorker;
  private boolean started = false;

  @PostConstruct
  public void start() {
    new Thread(this).start();
  }

  public void run() {

    if (started) {
      return;
    }

    started = true;

    sleep(10);

    logger.info("Start Monitor...");

    nebulaClient =
        new NebulaRestClient(nebulaServiceUsername, nebulaServicePassword, nebulaServiceHost,
                             nebulaServicePort, nebulaServiceContextPath);

    //startOneMinuteWorker();

    startStatisticWorker();

    logger.info("StatisticWorker started.");
  }

  private void startOneMinuteWorker() {

    List<String> realms = new ArrayList<String>();
    realms.add(OneMinuteWorker.class.getSimpleName());

    WorkflowWorker worker = new WorkflowWorker(nebulaClient,
                                               new Configuration());
    worker.add(OneMinuteWorkerImpl.class, realms);
    worker.start();
  }

  private void startStatisticWorker() {

    List<String> realms = new ArrayList<String>();
    realms.add(StatisticWorker.class.getSimpleName());

    activityWorker = new ActivityWorker(nebulaClient);
    activityWorker.add(StatisticActivityImpl.class, realms);
    activityWorker.add(SaveStatisticActivityImpl.class, realms);
    activityWorker.add(GetDomainsActivityImpl.class, realms);
    activityWorker.start();

    workflowWorker = new WorkflowWorker(nebulaClient,
                                        new Configuration());
    workflowWorker.add(StatisticWorkerImpl.class, realms);
    workflowWorker.start();
  }

  private void sleep(int secs) {
    try {
      Thread.sleep(secs * 1000);
    } catch (InterruptedException e) {
      //ignore
    }
  }

  @PreDestroy
  public void stop() {
    activityWorker.stop();
    workflowWorker.stop();
    started = false;
  }
}
