/* Create database nebula_monitor first before running the following DDL */

DROP DATABASE IF EXISTS nebula_monitor;
CREATE DATABASE nebula_monitor;

GRANT ALL PRIVILEGES ON nebula_monitor.* TO 'nebula'@'%';
GRANT ALL PRIVILEGES ON nebula_monitor.* TO 'nebula'@'localhost';
FLUSH PRIVILEGES;

USE nebula_monitor;

DROP TABLE IF EXISTS monitor_configs;

CREATE TABLE monitor_configs(
  id VARCHAR(40) NOT NULL  COMMENT 'monitor config id', 
  monitorId VARCHAR(40) NOT NULL COMMENT 'registration id or heartbeatId',
  monitorType VARCHAR(10) NOT NULL COMMENT 'PROCESS, ACTIVITY, HEARTBEAT',
  intervalMin INT NOT NULL COMMENT 'monitor interval: 1 or 5 minutes',
  data VARCHAR(10240) COMMENT 'config data',
  enabled TINYINT(1) NOT NULL COMMENT 'enable or not',
  createdDate DATETIME NOT NULL COMMENT 'config created date',
  updatedDate DATETIME NOT NULL COMMENT 'config modified date',
  PRIMARY KEY(id)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT 'monitor config table';

DROP TABLE IF EXISTS `statistics`;

CREATE TABLE `statistics` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'statistics id',
  `measurement` VARCHAR(20) NOT NULL COMMENT 'USER,UNCOMPLETED_INSTANCE,TOTAL_INSTANCE, WORKFLOW,ACTIVITY',
  `value` BIGINT(20) DEFAULT NULL COMMENT 'statistics value',
  `domainName`  VARCHAR(10) NOT NULL COMMENT 'domain name.',
  `username`  VARCHAR(20) NOT NULL COMMENT 'user name',
  `statisticDate` DATETIME NOT NULL COMMENT '0:00am everyday ,every week, every month or every year',
  `createdDate` DATETIME NOT NULL,
  `modifiedDate` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_idx_statistic` (`measurement`,`domainName`,`username`,`statisticDate`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
