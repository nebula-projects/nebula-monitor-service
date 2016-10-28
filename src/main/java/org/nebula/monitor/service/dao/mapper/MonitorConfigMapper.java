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
import org.apache.ibatis.session.RowBounds;
import org.nebula.monitor.service.dao.entity.MonitorConfig;

import java.util.List;

public interface MonitorConfigMapper {

  public void insert(MonitorConfig monitorConfig);

  public void update(MonitorConfig monitorConfig);

  public MonitorConfig find(String id);

  public List<MonitorConfig> findByMonitorIdAndMonitorType(
      RowBounds rowBounds, @Param("monitorId")
  String monitorId, @Param("monitorType")
      String monitorType);

  public int countByMonitorIdAndMonitorType(@Param("monitorId")
                                            String monitorId, @Param("monitorType")
                                            String monitorType);

  public List<MonitorConfig> findByIntervalMin(RowBounds rowBounds,
                                               @Param("intervalMin")
                                               int intervalMin);

  public int countByIntervalMin(int intervalMin);

  public void delete(@Param("id")
                     String id);
}
