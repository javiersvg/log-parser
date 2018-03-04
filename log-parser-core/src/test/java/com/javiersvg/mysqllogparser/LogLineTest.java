package com.javiersvg.mysqllogparser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class LogLineTest {

    @Mock
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void inRange() {
        String line = "2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"";

        LocalTime time = LocalTime.of(0,0,0);
        LocalDate date = LocalDate.of(2017,1,1);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        assertTrue(new LogLine(line, dataSource).inRange(dateTime, Duration.ofHours(1)));

        LocalTime time2 = LocalTime.of(23,0,10);
        LocalDate date2 = LocalDate.of(2016,12,31);
        LocalDateTime dateTime2 = LocalDateTime.of(date2, time2);
        assertFalse(new LogLine(line, dataSource).inRange(dateTime2, Duration.ofHours(1)));
    }

    @Test
    public void ipAddress() {
        String line = "2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"";
        assertThat(new LogLine(line, dataSource).ipAddress(), is(new IpAddress("192.168.234.82", dataSource)));
    }
}