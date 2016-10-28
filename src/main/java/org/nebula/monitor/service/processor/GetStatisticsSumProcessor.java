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

import org.nebula.admin.client.request.monitor.GetStatisticsSumRequest;
import org.nebula.admin.client.response.monitor.GetStatisticsSumResponse;
import org.nebula.monitor.service.dao.mapper.StatisticMapper;
import org.nebula.monitor.service.validator.GetStatisticsSumRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GetStatisticsSumProcessor extends
                                       AbstractProcessor<GetStatisticsSumRequest, GetStatisticsSumResponse> {

  @Autowired
  private StatisticMapper statisticMapper;

  @Autowired
  public GetStatisticsSumProcessor(GetStatisticsSumRequestValidator validator) {
    super(validator);
  }

  protected GetStatisticsSumResponse processInternal(GetStatisticsSumRequest request) {

    long
        number =
        statisticMapper
            .sum(request.getMeasurement(), request.getDomainName(), request.getUsername(),
                 request.getStartTimestamp() == null ? null : new Date(request.getStartTimestamp()),
                 request.getEndTimestamp() == null ? null : new Date(request.getEndTimestamp()));

    GetStatisticsSumResponse response = new GetStatisticsSumResponse();
    response.setNumber(number);

    return response;
  }
}
