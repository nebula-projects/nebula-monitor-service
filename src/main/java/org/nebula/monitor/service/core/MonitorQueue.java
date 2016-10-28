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

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MonitorQueue {

  private final static Logger logger = Logger.getLogger(MonitorQueue.class);

  private JedisPool jedisPool;

  private String queueKey;

  public MonitorQueue(JedisPool jedisPool, String queueName) {
    this.jedisPool = jedisPool;

    this.queueKey = queueName;
  }

  public long lengthOfQueue() {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      return jedis.llen(queueKey);
    } catch (Exception e) {
      logger.error("Failed to get the length of queue " + queueKey, e);
      jedisPool.returnBrokenResource(jedis);
    } finally {
      jedisPool.returnResource(jedis);
    }

    return -1;
  }

  public List<String> range(final long start, final long end) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      return jedis.lrange(queueKey, start, end);
    } catch (Exception e) {
      logger.error("Failed to get range from " + start + " to " + end
                   + " for queue " + queueKey, e);
      jedisPool.returnBrokenResource(jedis);
    } finally {
      jedisPool.returnResource(jedis);
    }

    return new ArrayList<String>();
  }

  public String trim(final long start, final long end) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      return jedis.ltrim(queueKey, start, end);
    } catch (Exception e) {
      logger.error("Failed to trim " + start + " to " + end
                   + " for queue " + queueKey, e);
      jedisPool.returnBrokenResource(jedis);
    } finally {
      jedisPool.returnResource(jedis);
    }

    return "-ERROR";
  }

  public void put(final String value) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      jedis.lpush(queueKey, value);

    } catch (Exception e) {
      logger.error("Failed to put  " + value + " to " + queueKey, e);
      jedisPool.returnBrokenResource(jedis);
    } finally {
      jedisPool.returnResource(jedis);
    }
  }

  public String get(final int timeout) throws Exception {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      List<String> values = jedis.brpop(timeout, queueKey);

      if (values == null || values.get(0) == null) {
        return null;
      }

      return values.get(1);
    } catch (Exception e) {
      logger.error("Failed to get message from  " + queueKey, e);
      jedisPool.returnBrokenResource(jedis);
    } finally {
      jedisPool.returnResource(jedis);
    }

    return null;
  }
}
