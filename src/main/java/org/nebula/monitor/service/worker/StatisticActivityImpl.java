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
import org.nebula.admin.client.NebulaMgmtClient;
import org.nebula.admin.client.model.admin.DomainSummary;
import org.nebula.admin.client.model.monitor.StatisticSummary;
import org.nebula.admin.client.request.mgmt.GetInstancesRequest;
import org.nebula.admin.client.request.mgmt.GetRegistrationsRequest;
import org.nebula.admin.client.response.mgmt.GetInstancesResponse;
import org.nebula.admin.client.response.mgmt.GetRegistrationsResponse;
import org.nebula.framework.client.request.RegisterRequest;
import org.nebula.framework.utils.JsonUtils;
import org.nebula.monitor.service.core.NebulaServiceClientConfig;
import org.nebula.monitor.service.dao.entity.Statistic;
import org.nebula.monitor.service.util.SpringInit;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivityImpl implements StatisticActivity {

  private final static Logger logger = Logger.getLogger(StatisticActivityImpl.class);

  private final static NebulaServiceClientConfig
      NEBULA_SERVICE_CLIENT_CONFIG =
      (NebulaServiceClientConfig) SpringInit
          .getApplicationContext().getBean("nebulaServiceClientConfig");

  private static Statistic getRegistrationStatistic(
      NebulaMgmtClient nebulaMgmtClient, String measurement, RegisterRequest.NodeType nodeType)
      throws Exception {

    Statistic statistic = new Statistic();
    statistic.setMeasurement(measurement);
    statistic.setValue((long) countRegistration(nebulaMgmtClient, nodeType));

    return statistic;
  }

  private static int countRegistration(
      NebulaMgmtClient nebulaMgmtClient, RegisterRequest.NodeType nodeType) throws Exception {

    GetRegistrationsRequest request = new GetRegistrationsRequest();
    request.setPageSize(1);
    request.setNodeType(nodeType);
    GetRegistrationsResponse response = nebulaMgmtClient.getRegistrations(request);

    return response.getTotal();
  }

  private static Statistic getInstanceStatistic(
      NebulaMgmtClient nebulaMgmtClient, String measurement,
      GetInstancesRequest.SearchMode searchMode) throws Exception {

    Statistic statistic = new Statistic();
    statistic.setMeasurement(measurement);
    statistic.setValue((long) countInstance(nebulaMgmtClient, searchMode));

    return statistic;
  }

  private static int countInstance(
      NebulaMgmtClient nebulaMgmtClient, GetInstancesRequest.SearchMode searchMode)
      throws Exception {

    GetInstancesRequest request = new GetInstancesRequest();
    request.setPageSize(1);
    request.setSearchMode(searchMode);
    GetInstancesResponse response = nebulaMgmtClient.getInstances(request);

    return response.getTotal();
  }

  public List<Statistic> collect(DomainSummary domainSummary) {

    logger.info("domainSummary=" + JsonUtils.toJson(domainSummary));

    List<Statistic> statistics = new ArrayList<Statistic>();

    if (serverAvailable(domainSummary.getServers())) {

      NebulaMgmtClient nebulaMgmtClient = new NebulaMgmtClient(
          NEBULA_SERVICE_CLIENT_CONFIG.getNebulaAdminUsername(),
          NEBULA_SERVICE_CLIENT_CONFIG.getNebulaAdminPassword(),
          domainSummary.getServers()[0], NEBULA_SERVICE_CLIENT_CONFIG.getPort(),
          NEBULA_SERVICE_CLIENT_CONFIG.getContextPath());

      statistics = collectStatistics(nebulaMgmtClient);
    }

    logger.info("Collect statistic for domain " + domainSummary.getName() + ", with server " +
                (domainSummary.getServers().length > 0 ? domainSummary.getServers()[0] : null)
                + ", result:" + JsonUtils.toJson(statistics));

    return statistics;
  }

  private boolean serverAvailable(String[] servers) {
    return servers.length > 0;
  }

  private List<Statistic> collectStatistics(NebulaMgmtClient nebulaMgmtClient) {

    List<Statistic> statistics = new ArrayList<Statistic>();

    try {
      statistics.add(
          getRegistrationStatistic(nebulaMgmtClient, StatisticSummary.WORKFLOWS_MEASUREMENT,
                                   RegisterRequest.NodeType.WORKFLOW));
      statistics.add(
          getRegistrationStatistic(nebulaMgmtClient, StatisticSummary.ACTIVITIES_MEASUREMENT,
                                   RegisterRequest.NodeType.ACTIVITY));
      statistics.add(
          getInstanceStatistic(nebulaMgmtClient, StatisticSummary.RUNNING_INSTANCES_MEASUREMENT,
                               GetInstancesRequest.SearchMode.RUNNING));
      statistics.add(
          getInstanceStatistic(nebulaMgmtClient, StatisticSummary.COMPLETED_INSTANCES_MEASUREMENT,
                               GetInstancesRequest.SearchMode.HISTORY));

    } catch (Exception e) {
      logger.error("Failed to collect,", e);
    }

    return statistics;
  }
}
