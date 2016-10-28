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

import org.springframework.core.env.Environment;

public class MonitorProperty {

  private static Environment env;

  public MonitorProperty(Environment environment) {
    System.out.println("env=" + environment);
    MonitorProperty.env = environment;
  }

  public static String getProperty(String key) {
    return env.getProperty(key);
  }

  public static <T> T getProperty(String key, Class<T> targetType) {
    return env.getProperty(key, targetType);
  }

  public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
    return env.getProperty(key, targetType, defaultValue);
  }

  public String getProperty(String key, String defaultValue) {
    return env.getProperty(key, defaultValue);
  }

}
