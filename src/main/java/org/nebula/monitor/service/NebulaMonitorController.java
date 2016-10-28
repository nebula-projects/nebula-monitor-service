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

package org.nebula.monitor.service;

import org.apache.log4j.Logger;
import org.nebula.admin.client.request.monitor.DeleteMonitorConfigRequest;
import org.nebula.admin.client.request.monitor.GetMonitorConfigRequest;
import org.nebula.admin.client.request.monitor.GetMonitorDataRequest;
import org.nebula.admin.client.request.monitor.GetStatisticsRequest;
import org.nebula.admin.client.request.monitor.GetStatisticsSumRequest;
import org.nebula.admin.client.request.monitor.SaveMonitorConfigRequest;
import org.nebula.admin.client.response.monitor.DeleteMonitorConfigResponse;
import org.nebula.admin.client.response.monitor.GetMonitorConfigResponse;
import org.nebula.admin.client.response.monitor.GetMonitorDataResponse;
import org.nebula.admin.client.response.monitor.GetStatisticsResponse;
import org.nebula.admin.client.response.monitor.GetStatisticsSumResponse;
import org.nebula.admin.client.response.monitor.SaveMonitorConfigResponse;
import org.nebula.monitor.service.processor.DeleteMonitorConfigProcessor;
import org.nebula.monitor.service.processor.GetMonitorConfigsProcessor;
import org.nebula.monitor.service.processor.GetMonitorDataProcessor;
import org.nebula.monitor.service.processor.GetStatisticsProcessor;
import org.nebula.monitor.service.processor.GetStatisticsSumProcessor;
import org.nebula.monitor.service.processor.SaveMonitorConfigProcessor;
import org.nebula.monitor.service.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NebulaMonitorController {

  private final static Logger logger = Logger
      .getLogger(NebulaMonitorController.class);

  @Autowired
  private SaveMonitorConfigProcessor saveMonitorConfigProcessor;

  @Autowired
  private GetMonitorConfigsProcessor getMonitorConfigProcessor;

  @Autowired
  private DeleteMonitorConfigProcessor deleteMonitorConfigProcessor;

  @Autowired
  private GetMonitorDataProcessor getMonitorDataProcessor;

  @Autowired
  private GetStatisticsProcessor getStatisticsProcessor;

  @Autowired
  private GetStatisticsSumProcessor getStatisticsSumProcessor;

  @RequestMapping(value = "/monitor/config", method = RequestMethod.POST)
  @ResponseBody
  public SaveMonitorConfigResponse saveMonitorConfig(@RequestBody
                                                     SaveMonitorConfigRequest request) {
    logger.info("saveMonitorConfig=" + JsonUtils.toJson(request));

    SaveMonitorConfigResponse response = null;
    try {
      response = saveMonitorConfigProcessor.process(request);
    } catch (Exception e) {
      logger.error("Failed to save monitor config", e);
      throw new RuntimeException(e);
    }

    logger.info("saveMonitorConfig response=" + JsonUtils.toJson(response));

    return response;

  }

  @RequestMapping(value = "/monitor/config", method = RequestMethod.GET)
  @ResponseBody
  public GetMonitorConfigResponse getMonitorConfigs(
      GetMonitorConfigRequest request) {
    logger.info("getMonitorConfigs=" + JsonUtils.toJson(request));

    GetMonitorConfigResponse response = getMonitorConfigProcessor
        .process(request);

    logger.info("getMonitorConfigs response=" + JsonUtils.toJson(response));

    return response;

  }

  @RequestMapping(value = "/monitor/config/delete", method = RequestMethod.POST)
  @ResponseBody
  DeleteMonitorConfigResponse deleteMonitorConfig(@RequestBody
                                                  DeleteMonitorConfigRequest request) {
    logger.info("deleteMonitorConfig=" + JsonUtils.toJson(request));

    DeleteMonitorConfigResponse response = deleteMonitorConfigProcessor
        .process(request);

    logger.info("deleteMonitorConfig response="
                + JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/monitor/data", method = RequestMethod.GET)
  @ResponseBody
  public GetMonitorDataResponse getMonitorData(GetMonitorDataRequest request) {
    logger.info("getMonitorData=" + JsonUtils.toJson(request));

    GetMonitorDataResponse response = getMonitorDataProcessor
        .process(request);

    logger.info("getMonitorData response=" + JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/monitor/statistics", method = RequestMethod.GET)
  @ResponseBody
  public GetStatisticsResponse getStatistics(GetStatisticsRequest request) {
    logger.info("getStatistics=" + JsonUtils.toJson(request));

    GetStatisticsResponse response = getStatisticsProcessor
        .process(request);

    logger.info("getStatistics response=" + JsonUtils.toJson(response));

    return response;
  }

  @RequestMapping(value = "/monitor/statisticssum", method = RequestMethod.GET)
  @ResponseBody
  public GetStatisticsSumResponse getStatisticsSum(GetStatisticsSumRequest request) {
    logger.info("getStatisticsSum=" + JsonUtils.toJson(request));

    GetStatisticsSumResponse response = getStatisticsSumProcessor
        .process(request);

    logger.info("getStatisticsSum response=" + JsonUtils.toJson(response));

    return response;
  }

}
