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
import org.nebula.admin.client.request.monitor.GetStatisticsSumRequest;
import org.springframework.stereotype.Component;

@Component
public class GetStatisticsSumRequestValidator implements
                                              Validator<GetStatisticsSumRequest> {

  public void validate(GetStatisticsSumRequest request) {
    Validate.notNull(request, "The GetStatisticsSumRequest can't be null");

    Validate.notEmpty(request.getMeasurement(),
                      "The GetStatisticsSumRequest's measurement can't be empty");
  }

}
