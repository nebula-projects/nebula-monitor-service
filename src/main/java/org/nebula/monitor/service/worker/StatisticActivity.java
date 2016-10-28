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

import org.nebula.admin.client.model.admin.DomainSummary;
import org.nebula.framework.annotation.Activity;
import org.nebula.monitor.service.dao.entity.Statistic;

import java.util.List;

@Activity
public interface StatisticActivity {

  public List<Statistic> collect(DomainSummary domainSummary);
}
