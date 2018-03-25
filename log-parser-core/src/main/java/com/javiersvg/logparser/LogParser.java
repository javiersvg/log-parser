package com.javiersvg.logparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LogParser {
    private static final String ERROR_PROPERTIES_FILE_KEY = "error.properties.file";
    private static final String ERROR_LOG_FILE_KEY = "error.log.file";
    private static final String CONFIG_PROPERTIES_KEY = "config.properties";
    private static final String LOG_FILE_KEY = "logFile";
    private final LocalDateTime startDate;
    private final int threshold;
    private Duration duration;
    private Properties properties = new Properties();
    private DataSourceFactory dataSourceFactory;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

    public LogParser(LocalDateTime startDate, Duration duration, int threshold) {
        this.startDate = startDate;
        this.duration = duration;
        this.threshold = threshold;
        try {
            InputStream resource = getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_KEY);
            if (resource != null) {
                properties.load(new InputStreamReader(resource));
            } else {
                throw new LogParserException(resourceBundle.getString(ERROR_PROPERTIES_FILE_KEY));
            }
        } catch (IOException e) {
            throw new LogParserException(resourceBundle.getString(ERROR_PROPERTIES_FILE_KEY), e);
        }
        this.dataSourceFactory = new CacheablesDataSourceFactoryDecorator(new ParserDataSourceFactory(properties));
    }

    public void parse() {
        try (BufferedReader bufferedReader = new BufferedReader(logFile())) {
            bufferedReader.lines()
                    .map(line -> new LogLine(line, dataSourceFactory.dataSource()))
                    .filter(logLine -> logLine.inRange(startDate, duration))
                    .collect(Collectors.groupingBy(LogLine::ipAddress, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() > threshold)
                    .forEach(entry -> entry.getKey().block(startDate, duration, entry.getValue()));
        } catch (IOException e) {
            throw new LogParserException(resourceBundle.getString(ERROR_LOG_FILE_KEY), e);
        }
    }

    private Reader logFile() {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(properties.getProperty(LOG_FILE_KEY));
        if (resource != null) {
            return new InputStreamReader(resource);
        } else {
            throw new LogParserException(resourceBundle.getString(ERROR_PROPERTIES_FILE_KEY));
        }
    }
}
