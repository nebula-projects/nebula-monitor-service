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

package org.nebula.monitor.service;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@EnableWebMvc
public class NebulaMonitorExceptionHandler extends ResponseEntityExceptionHandler {

  private final static Logger logger = Logger
      .getLogger(NebulaMonitorExceptionHandler.class);

  @ExceptionHandler({IllegalArgumentException.class})
  protected ResponseEntity<Object> badRequest(IllegalArgumentException ex,
                                              WebRequest request) {
    String bodyOfResponse = ex.getMessage();

    ResponseEntity<Object> response = handleExceptionInternal(null,
                                                              bodyOfResponse, new HttpHeaders(),
                                                              HttpStatus.BAD_REQUEST,
                                                              request);

    logger.error("Exception:" + response.toString(), ex);

    return response;
  }

  @ExceptionHandler({Exception.class})
  protected ResponseEntity<Object> exceptions(Exception ex, WebRequest request) {
    String bodyOfResponse = ex.getMessage();

    ResponseEntity<Object> response = handleExceptionInternal(null,
                                                              bodyOfResponse, new HttpHeaders(),
                                                              HttpStatus.INTERNAL_SERVER_ERROR,
                                                              request);

    logger.error("Exception:" + response.toString(), ex);

    return response;
  }
}