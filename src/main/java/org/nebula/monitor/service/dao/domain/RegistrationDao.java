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

package org.nebula.monitor.service.dao.domain;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationDao {

  private JdbcTemplate jdbcTemplate;

  public RegistrationDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Page<String> getDistinctUsers(int pageNo, int pageSize) {
    PaginationHelper<String> ph = new PaginationHelper<String>();
    return ph.fetchPage(
        jdbcTemplate,
        "SELECT count(distinct user) FROM registrations ORDER BY user",
        "SELECT distinct user FROM registrations ORDER BY user",
        null,
        pageNo,
        pageSize,
        new ParameterizedRowMapper<String>() {
          public String mapRow(ResultSet rs, int i) throws SQLException {
            return rs.getString(1);
          }
        }
    );
  }

}
