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

package org.nebula.monitor.service.processor;

import org.nebula.admin.client.model.monitor.MonitorData;
import org.nebula.admin.client.request.monitor.GetMonitorDataRequest;
import org.nebula.admin.client.response.monitor.GetMonitorDataResponse;
import org.nebula.monitor.service.core.MonitorDataQueueName;
import org.nebula.monitor.service.core.MonitorQueue;
import org.nebula.monitor.service.util.JsonUtils;
import org.nebula.monitor.service.validator.GetMonitorDataRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisPool;

@Component
public class GetMonitorDataProcessor extends
                                     AbstractProcessor<GetMonitorDataRequest, GetMonitorDataResponse> {

  @Autowired
  private JedisPool jedisPool;

  @Autowired
  public GetMonitorDataProcessor(GetMonitorDataRequestValidator validator) {
    super(validator);
  }

  public GetMonitorDataResponse processInternal(GetMonitorDataRequest request) {

    MonitorQueue monitorDataQueue = new MonitorQueue(jedisPool,
                                                     new MonitorDataQueueName(request.getConfigId())
                                                         .toString());

    long total = monitorDataQueue.lengthOfQueue();

    long pageNo = request.getPageNo();
    long pageSize = request.getPageSize();

    long start = (pageNo - 1) * pageSize;
    long end = pageNo * pageSize - 1;

    List<String> dataList = monitorDataQueue.range(start, end);

    List<MonitorData> monitorDataList = new ArrayList<MonitorData>();
    for (String data : dataList) {
      MonitorData monitorData = (MonitorData) JsonUtils.toObject(data,
                                                                 MonitorData.class);

      monitorDataList.add(monitorData);
    }

    GetMonitorDataResponse response = new GetMonitorDataResponse();
    response.setMonitorData(monitorDataList);
    response.setTotal(total);
    response.setPageNo(pageNo);
    response.setPageSize(pageSize);
    return response;
  }
}
