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
import org.nebula.admin.client.request.monitor.GetMonitorDataRequest;
import org.springframework.stereotype.Component;

@Component
public class GetMonitorDataRequestValidator implements
                                            Validator<GetMonitorDataRequest> {

  public void validate(GetMonitorDataRequest request) {
    Validate.notNull(request, "The GetMonitorDataRequest can't be null.");

    Validate.notEmpty(request.getConfigId(), "The configId can't be empty.");

    Validate.isTrue(request.getPageNo() > 0, "The pageNo must be greater than zero.");

    Validate.isTrue(request.getPageSize() > 0, "The pageSize must be greater than zero.");
  }

}
