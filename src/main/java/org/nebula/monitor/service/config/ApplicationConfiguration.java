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

package org.nebula.monitor.service.config;

import org.nebula.admin.client.NebulaAdminClient;
import org.nebula.monitor.service.core.NebulaServiceClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class ApplicationConfiguration {

  @Value("${redis.host}")
  String redisHost;

  @Value("${redis.port}")
  int redisPort;

  @Value("${redis.maxTotal}")
  int redisMaxTotal;

  @Value("${redis.maxIdle}")
  int redisMaxIdle;

  @Value("${redis.maxWaitSecs}")
  int redisMaxWaitSecs;

  @Value("${nebula.admin.service.username}")
  String nebulaAdminServiceUsername;

  @Value("${nebula.admin.service.password}")
  String nebulaAdminServicePassword;

  @Value("${nebula.admin.service.host}")
  String nebulaAdminServiceHost;

  @Value("${nebula.admin.service.port}")
  int nebulaAdminServicePort;

  @Value("${nebula.admin.service.contextPath}")
  String nebulaAdminServiceContextPath;

  @Value("${nebula.service.username}")
  String nebulaServiceUsername;

  @Value("${nebula.service.password}")
  String nebulaServicePassword;

  @Value("${nebula.service.port}")
  int nebulaServicePort;

  @Value("${nebula.service.contextPath}")
  String nebulaServiceContextPath;

  @Autowired
  Environment env;


  @Bean
  JedisPool jedisPool() {
    JedisPoolConfig config = new JedisPoolConfig();

    config.setMaxTotal(redisMaxTotal);
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
    config.setMaxIdle(redisMaxIdle);
    //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
    config.setMaxWaitMillis(1000 * redisMaxWaitSecs);
    config.setTestOnBorrow(true);

    return new JedisPool(config, redisHost, redisPort);
  }

  @Bean
  NebulaAdminClient nebulaAdminClient() {
    return new NebulaAdminClient(nebulaAdminServiceUsername, nebulaAdminServicePassword,
                                 nebulaAdminServiceHost, nebulaAdminServicePort,
                                 nebulaAdminServiceContextPath);
  }

  @Bean
  NebulaServiceClientConfig nebulaServiceClientConfig() {
    return new NebulaServiceClientConfig(nebulaServiceUsername, nebulaServicePassword,
                                         nebulaServicePort, nebulaServiceContextPath);
  }

}
