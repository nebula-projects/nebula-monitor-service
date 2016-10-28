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
import org.nebula.admin.client.NebulaAdminClient;
import org.nebula.admin.client.model.admin.DomainSummary;
import org.nebula.admin.client.request.admin.GetDomainsRequest;
import org.nebula.admin.client.response.admin.GetDomainsResponse;
import org.nebula.monitor.service.util.SpringInit;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * We assume that the number of domains has a reasonable limit. e.g. <10000. So we can store all
 * domains into memory and don't worry about the OOM.
 */
public class GetDomainsActivityImpl implements GetDomainsActivity {

  private final static Logger logger = Logger.getLogger(GetDomainsActivityImpl.class);

  private static final int PAGE_SIZE = 100;

  public List<DomainSummary> getDomains() {

    NebulaAdminClient nebulaAdminClient = (NebulaAdminClient) SpringInit
        .getApplicationContext().getBean("nebulaAdminClient");

    logger.info("Start to get domains.");

    List<DomainSummary> domainSummaries = new ArrayList<DomainSummary>();

    int pageNo = 0;
    while (true) {

      GetDomainsRequest request = new GetDomainsRequest();
      request.setPageNo(pageNo);
      request.setPageSize(PAGE_SIZE);

      GetDomainsResponse response;
      try {
        response = nebulaAdminClient.getDomains(request);
      } catch (Exception e) {
        logger.error("Failed to get domains from nebula admin service.", e);
        //TODO  return domainSummaries?
        return new ArrayList<DomainSummary>();
      }

      domainSummaries.addAll(response.getDomainSummaries());

      if (domainSummaries.size() < PAGE_SIZE) {
        break;
      }
      pageNo++;
    }

    return domainSummaries;

  }

}
