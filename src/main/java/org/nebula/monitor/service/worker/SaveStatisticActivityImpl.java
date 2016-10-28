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
import org.nebula.monitor.service.dao.entity.Statistic;
import org.nebula.monitor.service.dao.mapper.StatisticMapper;
import org.nebula.monitor.service.util.JsonUtils;
import org.nebula.monitor.service.util.SpringInit;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collection;

public class SaveStatisticActivityImpl implements SaveStatisticActivity {

  private final static Logger logger = Logger.getLogger(SaveStatisticActivityImpl.class);

  private final static StatisticMapper statisticMapper = (StatisticMapper) SpringInit
      .getApplicationContext().getBean("statisticMapper");

  public void save(Collection<Statistic> statistics) {

    for (Statistic statistic : statistics) {
      try {
        statisticMapper.insert(statistic);
      } catch (DuplicateKeyException e) {
        logger.warn("Duplicate Key for statistic:" + JsonUtils.toJson(statistic));

        //If statistic value is zero, don't update it.
        if (statistic.getValue() > 0) {
          statisticMapper.update(statistic);
        }
      }
    }

    logger.info("The statistic saved.");
  }

}
