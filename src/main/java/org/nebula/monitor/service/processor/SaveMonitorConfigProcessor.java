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
import org.nebula.admin.client.model.monitor.MonitorConfigSummary;
import org.nebula.admin.client.request.monitor.SaveMonitorConfigRequest;
import org.nebula.admin.client.response.monitor.SaveMonitorConfigResponse;
import org.nebula.monitor.service.dao.entity.MonitorConfig;
import org.nebula.monitor.service.dao.mapper.MonitorConfigMapper;
import org.nebula.monitor.service.util.JsonUtils;
import org.nebula.monitor.service.validator.SaveMonitorConfigRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SaveMonitorConfigProcessor extends
                                        AbstractProcessor<SaveMonitorConfigRequest, SaveMonitorConfigResponse> {

  @Autowired
  private MonitorConfigMapper monitorConfigMapper;

  @Autowired
  public SaveMonitorConfigProcessor(SaveMonitorConfigRequestValidator validator) {
    super(validator);
  }

  public SaveMonitorConfigResponse processInternal(
      SaveMonitorConfigRequest request) {

    MonitorConfigSummary summary = request.getMonitorConfigSummary();
    MonitorConfig monitorConfig = transform(summary);

    if (StringUtils.isBlank(monitorConfig.getId())) {
      monitorConfig.setId(UUID.randomUUID().toString());
      summary.setId(monitorConfig.getId());
      monitorConfig.setData(JsonUtils.toJson(summary));
      monitorConfigMapper.insert(monitorConfig);
    } else {
      monitorConfig.setData(JsonUtils.toJson(summary));
      monitorConfigMapper.update(monitorConfig);
    }

    SaveMonitorConfigResponse response = new SaveMonitorConfigResponse();
    response.setId(monitorConfig.getId());

    return response;
  }

  private MonitorConfig transform(MonitorConfigSummary summary) {
    MonitorConfig monitorConfig = new MonitorConfig();

    monitorConfig.setId(summary.getId());
    monitorConfig.setMonitorId(summary.getMonitorId());
    monitorConfig.setIntervalMin(summary.getIntervalMin());
    monitorConfig.setMonitorType(summary.getMonitorType().name());
    monitorConfig.setEnabled(summary.isEnabled());
    monitorConfig.setCreatedDate(summary.getCreatedDate());
    monitorConfig.setUpdatedDate(summary.getUpdatedDate());
    monitorConfig.setData(JsonUtils.toJson(summary));

    return monitorConfig;
  }
}
