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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CustomPropertyPlaceholderConfigurer extends
                                                 PropertyPlaceholderConfigurer {

  private static Map<String, Object> ctxPropertiesMap;

  public static Object getProperty(String name) {
    return ctxPropertiesMap.get(name);
  }

  public static String getString(String name) {
    return (String) ctxPropertiesMap.get(name);
  }

  public static int getInt(String name) {
    return Integer.parseInt(ctxPropertiesMap.get(name).toString());
  }

  @Override
  protected void processProperties(
      ConfigurableListableBeanFactory beanFactoryToProcess,
      Properties props) throws BeansException {
    super.processProperties(beanFactoryToProcess, props);
    ctxPropertiesMap = new HashMap<String, Object>();
    for (Object key : props.keySet()) {
      String keyStr = key.toString();
      String value = props.getProperty(keyStr);
      ctxPropertiesMap.put(keyStr, value);
    }
  }
}