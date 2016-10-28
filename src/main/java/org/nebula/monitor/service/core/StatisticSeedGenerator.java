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

import org.nebula.monitor.service.util.SpringInit;

import redis.clients.jedis.JedisPool;

public class StatisticSeedGenerator {

  private MonitorQueue monitorQueue;

  public StatisticSeedGenerator() {
    JedisPool jedisPool = (JedisPool) SpringInit
        .getApplicationContext().getBean("jedisPool");

    monitorQueue = new MonitorQueue(jedisPool, QueueName.getStatisticQueueName());
  }

  public void emit() {
    monitorQueue.put(Statistic.ALL_WORKFLOWS_COUNT);
    monitorQueue.put(Statistic.ALL_ACTIVITIES_COUNT);
    monitorQueue.put(Statistic.ALL_RUNNING_INSTANCES_COUNT);
    monitorQueue.put(Statistic.ALL_COMPLTED_INSTANCES_COUNT);
  }
}
