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

package org.nebula.monitor.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.log4j.Logger;

public class JsonUtils {

  private final static Logger logger = Logger
      .getLogger(JsonUtils.class);

  private final static ObjectMapper mapper = new ObjectMapper();

  public final static String toJson(Object obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      logger.error("Failed to convert the object " + obj + " to json.", e);
    }
    return "";
  }

  public final static Object toObject(String json, Class clazz) {
    try {
      return mapper.readValue(json, clazz);
    } catch (Exception e) {
      logger.error("Failed to convert the json " + json + " to object " + clazz, e);
    }
    return "";
  }
}
