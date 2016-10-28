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

package org.nebula.monitor.service.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

  public static Date today() {

    GregorianCalendar now = new GregorianCalendar();
    now.set(Calendar.HOUR_OF_DAY, 0);
    now.set(Calendar.MINUTE, 0);
    now.set(Calendar.SECOND, 0);
    now.set(Calendar.MILLISECOND, 0);

    return now.getTime();
  }

  public static Date getNextStatisticDate(Date latestStatisticDate) {

    GregorianCalendar nextStatiticDate;
    if (latestStatisticDate != null) {
      nextStatiticDate = new GregorianCalendar();
      nextStatiticDate.setTime(latestStatisticDate);
      nextStatiticDate.add(Calendar.DATE, 1);
    } else {
      //We set the date which is meaningful to me before nebula is ready.
      nextStatiticDate = new GregorianCalendar(2012, 6, 15);
    }

    return nextStatiticDate.getTime();
  }
}
