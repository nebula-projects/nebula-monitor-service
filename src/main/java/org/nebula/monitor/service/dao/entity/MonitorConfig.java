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

package org.nebula.monitor.service.dao.entity;

import java.util.Date;

public class MonitorConfig {

  private String id;

  private String monitorId;

  private String monitorType;

  private int intervalMin;

  private String data;

  private boolean enabled;

  private Date createdDate;

  private Date updatedDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMonitorId() {
    return monitorId;
  }

  public void setMonitorId(String monitorId) {
    this.monitorId = monitorId;
  }

  public String getMonitorType() {
    return monitorType;
  }

  public void setMonitorType(String monitorType) {
    this.monitorType = monitorType;
  }

  public int getIntervalMin() {
    return intervalMin;
  }

  public void setIntervalMin(int intervalMin) {
    this.intervalMin = intervalMin;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

}
