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

package org.nebula.monitor.service.worker;

import org.apache.log4j.Logger;
import org.nebula.admin.client.model.admin.DomainSummary;
import org.nebula.framework.core.Promise;
import org.nebula.monitor.service.dao.entity.Statistic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nebula.admin.client.model.monitor.StatisticSummary.ACTIVITIES_MEASUREMENT;
import static org.nebula.admin.client.model.monitor.StatisticSummary.COMPLETED_INSTANCES_MEASUREMENT;
import static org.nebula.admin.client.model.monitor.StatisticSummary.RUNNING_INSTANCES_MEASUREMENT;
import static org.nebula.admin.client.model.monitor.StatisticSummary.WORKFLOWS_MEASUREMENT;
import static org.nebula.monitor.service.util.DateUtil.today;

public class StatisticWorkerImpl implements StatisticWorker {

  private final static Logger logger = Logger.getLogger(StatisticWorkerImpl.class);
  private final static String[] MEASUREMENTS = new String[]{
      WORKFLOWS_MEASUREMENT, ACTIVITIES_MEASUREMENT,
      RUNNING_INSTANCES_MEASUREMENT, COMPLETED_INSTANCES_MEASUREMENT};
  private GetDomainsActivityClient getDomainsClient = new GetDomainsActivityClientImpl();
  private StatisticActivityClient statisticActivityClient = new StatisticActivityClientImpl();
  private SaveStatisticActivityClient
      saveStatisticActivityClient =
      new SaveStatisticActivityClientImpl();

  public void start() {

    Promise<List<DomainSummary>> domains = getDomainsClient.getDomains();

    if (domains.isReady()) {

      Map<String, Statistic> statistics = createStatistics();

      List<DomainSummary> domainSummaries = domains.get();

      int processedCount = 0;

      for (DomainSummary domainSummary : domainSummaries) {

        Promise<List<Statistic>> domainStatistics = statisticActivityClient.collect(domainSummary);

        if (domainStatistics.isReady()) {
          for (Statistic statistic : domainStatistics.get()) {

            Statistic sumStatistic = statistics.get(statistic.getMeasurement());
            if (sumStatistic != null) {
              sumStatistic.setValue(sumStatistic.getValue() + statistic.getValue());
            } else {
              logger.error(
                  "The statistic measurement " + statistic.getMeasurement() + " is illegal.");
            }

          }

          processedCount++;
        }
      }

      if (processedCount == domainSummaries.size()) {

        saveStatisticActivityClient.save(statistics.values());

        logger.info("Statistic Worker Completed.");
      }
    }

  }

  private Map<String, Statistic> createStatistics() {
    Map<String, Statistic> statistics = new HashMap<String, Statistic>();

    for (String measurement : MEASUREMENTS) {
      statistics.put(measurement, createStatistic(measurement));
    }

    return statistics;
  }

  private Statistic createStatistic(String measurement) {
    Statistic statistic = new Statistic();
    statistic.setMeasurement(measurement);
    statistic.setStatisticDate(today());
    return statistic;
  }

}
