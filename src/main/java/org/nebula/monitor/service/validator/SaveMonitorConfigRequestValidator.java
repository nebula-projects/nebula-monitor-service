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

package org.nebula.monitor.service.validator;

import org.apache.commons.lang.Validate;
import org.nebula.admin.client.model.monitor.MonitorConfigSummary;
import org.nebula.admin.client.request.monitor.SaveMonitorConfigRequest;
import org.springframework.stereotype.Component;

@Component
public class SaveMonitorConfigRequestValidator implements
                                               Validator<SaveMonitorConfigRequest> {

  public void validate(SaveMonitorConfigRequest request) {
    Validate.notNull(request, "The SaveMonitorConfigRequest can't be null");

    MonitorConfigSummary monitorConfigSummary = request
        .getMonitorConfigSummary();

    Validate.notNull(monitorConfigSummary,
                     "The SaveMonitorConfigRequest's monitorConfigSummary can't be null");

    Validate.notEmpty(monitorConfigSummary.getMonitorId(),
                      "The monitorConfigSummary's monitorId can't be empty.");

    Validate.notNull(monitorConfigSummary.getMonitorType(),
                     "The monitorConfigSummary's monitorType can't be null.");

    Validate.isTrue(monitorConfigSummary.getIntervalMin() == 1
                    || monitorConfigSummary.getIntervalMin() == 5,
                    "The monitorConfigSummary's interval must be 1 or 5.");

    Validate.isTrue(monitorConfigSummary.getConsecutiveTimes() > 0,
                    "The consecutive times must be greater than zero.");

    if (monitorConfigSummary.getMonitorType() == MonitorConfigSummary.MonitorType.PROCESS) {
      Validate.isTrue(monitorConfigSummary.getDuration() > 0,
                      "The duration must be greater than zero.");
      Validate.notNull(monitorConfigSummary.getTimeUnit(),
                       "The timeUnit can't be null.");
    }

    Validate.notNull(monitorConfigSummary.getPriority(),
                     "The priority can't be null.");

    Validate.isTrue(monitorConfigSummary.getUncompletedNumber() > 0,
                    "The uncompleted number must be greater than zero.");
  }

}
