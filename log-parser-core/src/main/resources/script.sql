## schema, table and user creation

CREATE SCHEMA `test` ;

CREATE TABLE `test`.`BLOCKED_IPS` (
  `IP` VARCHAR(15) NOT NULL,
  `OCCURRENCES` INT NULL,
  `DATE` TIMESTAMP NULL,
  `REASON` VARCHAR(100) NULL,
  PRIMARY KEY (`IP`));

CREATE USER test@'%' identified by 'ch4ng3m3';

GRANT SELECT,INSERT on test.BLOCKED_IPS to test@'%';

## QUERIES

# 1) Write MySQL query to find IPs that mode more than a certain number of requests for a given time period.
SELECT * FROM BLOCKED_IPS WHERE OCCURRENCES > 100 AND DATE BETWEEN '2017-01-01.13:00:00' AND '2017-01-01.14:00:00';

# 2) Write MySQL query to find requests made by a given IP.
SELECT * FROM BLOCKED_IPS WHERE IP = '192.168.228.188'