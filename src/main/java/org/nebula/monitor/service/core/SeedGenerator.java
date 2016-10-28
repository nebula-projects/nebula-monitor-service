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

import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.nebula.monitor.service.dao.entity.MonitorConfig;
import org.nebula.monitor.service.dao.mapper.MonitorConfigMapper;
import org.nebula.monitor.service.util.SpringInit;

import java.util.List;

import redis.clients.jedis.JedisPool;

public class SeedGenerator {

  public final static String MONITOR_CONFIG_QUEUE = "MONITOR_CONFIG_QUEUE";
  private final static Logger logger = Logger.getLogger(SeedGenerator.class);
  private final static int PAGE_SIZE = 10;

  private MonitorConfigMapper monitorConfigMapper;

  private MonitorQueue monitorConfigQueue;

  public SeedGenerator() {

    monitorConfigMapper = (MonitorConfigMapper) SpringInit
        .getApplicationContext().getBean("monitorConfigMapper");

    JedisPool jedisPool = (JedisPool) SpringInit
        .getApplicationContext().getBean("jedisPool");

    monitorConfigQueue = new MonitorQueue(jedisPool, MONITOR_CONFIG_QUEUE);
  }

  public void process(int intervalMin) {

    logger.info("Start to generate monitor seed for interval " + intervalMin);

    int pageNo = 0;
    int total = monitorConfigMapper.countByIntervalMin(intervalMin);

    while (pageNo <= totalPages(total)) {
      List<MonitorConfig> monitorConfigs = scan(intervalMin, pageNo, PAGE_SIZE);

      for (MonitorConfig monitorConfig : monitorConfigs) {
        monitorConfigQueue.put(monitorConfig.getData());
      }

      pageNo++;
    }
  }

  private List<MonitorConfig> scan(int intervalMin, int pageNo, int pageSize) {

    RowBounds rowBounds = new RowBounds(pageNo * pageSize, pageSize);
    return monitorConfigMapper.findByIntervalMin(rowBounds, intervalMin);
  }

  private int totalPages(int total) {
    return (int) Math.ceil(1.0 * total / PAGE_SIZE);
  }
}
