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

package org.nebula.monitor.service.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.nebula.monitor.service.dao.entity.Statistic;

import java.util.Date;
import java.util.List;

public interface StatisticMapper {

  public Long insert(Statistic statistic);

  public void update(Statistic statistic);

  public Statistic find(Long id);

  public long count(@Param("measurement") String measurement,
                    @Param("domainName") String domainName, @Param("username") String username,
                    @Param("startDate") Date startDate, @Param("endDate") Date endDate);

  public List<Statistic> list(@Param("measurement") String measurement,
                              @Param("domainName") String domainName,
                              @Param("username") String username,
                              @Param("startDate") Date startDate, @Param("endDate") Date endDate);

  public Date getLatestStatisticDate(@Param("measurement") String measurement,
                                     @Param("domainName") String domainName,
                                     @Param("username") String username);

  public long sum(@Param("measurement") String measurement, @Param("domainName") String domainName,
                  @Param("username") String username, @Param("startDate") Date startDate,
                  @Param("endDate") Date endDate);
}
