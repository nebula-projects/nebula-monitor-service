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

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.nebula.admin.client.model.monitor.MonitorConfigSummary;
import org.nebula.admin.client.request.monitor.GetMonitorConfigRequest;
import org.nebula.admin.client.response.monitor.GetMonitorConfigResponse;
import org.nebula.monitor.service.dao.entity.MonitorConfig;
import org.nebula.monitor.service.dao.mapper.MonitorConfigMapper;
import org.nebula.monitor.service.util.JsonUtils;
import org.nebula.monitor.service.validator.GetMonitorConfigRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetMonitorConfigsProcessor extends
                                        AbstractProcessor<GetMonitorConfigRequest, GetMonitorConfigResponse> {

  @Autowired
  private MonitorConfigMapper monitorConfigMapper;

  @Autowired
  public GetMonitorConfigsProcessor(GetMonitorConfigRequestValidator validator) {
    super(validator);
  }

  public GetMonitorConfigResponse processInternal(
      GetMonitorConfigRequest request) {

    GetMonitorConfigResponse response = new GetMonitorConfigResponse();

    List<MonitorConfig> monitorConfigs = new ArrayList<MonitorConfig>();

    if (StringUtils.isBlank(request.getConfigId())) {

      RowBounds rowBounds = new RowBounds((request.getPageNo() - 1)
                                          * request.getPageSize(), request.getPageSize());

      monitorConfigs.addAll(monitorConfigMapper
                                .findByMonitorIdAndMonitorType(rowBounds, request
                                    .getMonitorId(), request.getMonitorType()
                                                                   .name()));

      response.setTotal(monitorConfigMapper
                            .countByMonitorIdAndMonitorType(request
                                                                .getMonitorId(),
                                                            request.getMonitorType()
                                                                .name()));
    } else {

      MonitorConfig monitorConfig = monitorConfigMapper.find(request
                                                                 .getConfigId());
      if (monitorConfig != null) {
        monitorConfigs.add(monitorConfig);
        response.setTotal(1);
      }
    }

    response.setMonitorConfigSummaries(transform(monitorConfigs));

    return response;
  }

  private List<MonitorConfigSummary> transform(
      List<MonitorConfig> monitorConfigs) {

    List<MonitorConfigSummary> monitorConfigSummaries = new ArrayList<MonitorConfigSummary>();

    for (MonitorConfig monitorConfig : monitorConfigs) {

      MonitorConfigSummary monitorConfigSummary = (MonitorConfigSummary) JsonUtils
          .toObject(monitorConfig.getData(),
                    MonitorConfigSummary.class);

      monitorConfigSummary.setCreatedDate(monitorConfig.getCreatedDate());
      monitorConfigSummary.setUpdatedDate(monitorConfig.getUpdatedDate());
      monitorConfigSummaries.add(monitorConfigSummary);
    }

    return monitorConfigSummaries;
  }
}
