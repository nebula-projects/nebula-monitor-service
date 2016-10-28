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

import org.nebula.admin.client.model.monitor.StatisticSummary;
import org.nebula.admin.client.request.monitor.GetStatisticsRequest;
import org.nebula.admin.client.response.monitor.GetStatisticsResponse;
import org.nebula.monitor.service.dao.entity.Statistic;
import org.nebula.monitor.service.dao.mapper.StatisticMapper;
import org.nebula.monitor.service.validator.GetStatisticsRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GetStatisticsProcessor extends
                                    AbstractProcessor<GetStatisticsRequest, GetStatisticsResponse> {

  @Autowired
  private StatisticMapper statisticMapper;

  @Autowired
  public GetStatisticsProcessor(GetStatisticsRequestValidator validator) {
    super(validator);
  }

  /**
   * TODO If the range between startTimestamp and endTimestamp, the results may be very large. Add
   * the pagination support in the future..
   */
  protected GetStatisticsResponse processInternal(GetStatisticsRequest request) {

    List<Statistic>
        statistics =
        statisticMapper
            .list(request.getMeasurement(), request.getDomainName(), request.getUsername(),
                  request.getStartTimestamp() == null ? null
                                                      : new Date(request.getStartTimestamp()),
                  request.getEndTimestamp() == null ? null : new Date(request.getEndTimestamp()));

    List<StatisticSummary> summaries = new ArrayList<StatisticSummary>();
    for (Statistic statistic : statistics) {
      StatisticSummary summary = new StatisticSummary();
      summary.setId(statistic.getId());
      summary.setMeasurement(statistic.getMeasurement());
      summary.setDomainName(statistic.getDomainName());
      summary.setUsername(statistic.getUsername());
      summary.setValue(statistic.getValue());
      summary.setStatisticDate(statistic.getStatisticDate());

      summaries.add(summary);
    }

    GetStatisticsResponse response = new GetStatisticsResponse();
    response.setStatisticSummaries(summaries);

    return response;
  }
}
