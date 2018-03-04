package com.javiersvg.mysqllogparser;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LogLine {
    private Pattern pattern = Pattern.compile("^(.*)\\|(.*)\\|(.*)\\|(.*)\\|");
    private String line;
    private DataSource dataSource;

    LogLine(String line, DataSource dataSource) {
        this.line = line;
        this.dataSource = dataSource;
    }

    boolean inRange(LocalDateTime startDate, Duration duration) {
        LocalDateTime logDate = logDate();
        LocalDateTime endDate = startDate.plus(duration);
        return logDate.isAfter(startDate) && logDate.isBefore(endDate);
    }

    IpAddress ipAddress() {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return new IpAddress(matcher.group(2), dataSource);
        } else {
            throw new MySqlLogParserException("No IP found in line: " + line);
        }
    }

    private LocalDateTime logDate() {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return LocalDateTime.parse(matcher.group(1).replace( " " , "T" ));
        } else {
            throw new MySqlLogParserException("No date in line: " + line);
        }
    }
}