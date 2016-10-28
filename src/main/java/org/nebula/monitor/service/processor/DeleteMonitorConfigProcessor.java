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

import org.nebula.admin.client.request.monitor.DeleteMonitorConfigRequest;
import org.nebula.admin.client.response.monitor.DeleteMonitorConfigResponse;
import org.nebula.monitor.service.dao.mapper.MonitorConfigMapper;
import org.nebula.monitor.service.validator.DeleteMonitorConfigRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteMonitorConfigProcessor
    extends
    AbstractProcessor<DeleteMonitorConfigRequest, DeleteMonitorConfigResponse> {

  @Autowired
  private MonitorConfigMapper monitorConfigMapper;

  @Autowired
  public DeleteMonitorConfigProcessor(DeleteMonitorConfigRequestValidator validator) {
    super(validator);
  }

  public DeleteMonitorConfigResponse processInternal(
      DeleteMonitorConfigRequest request) {

    monitorConfigMapper.delete(request.getConfigId());

    DeleteMonitorConfigResponse response = new DeleteMonitorConfigResponse();
    response.setConfigId(request.getConfigId());

    return response;
  }
}
