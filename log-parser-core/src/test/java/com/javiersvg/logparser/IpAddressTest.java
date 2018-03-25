package com.javiersvg.logparser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IpAddressTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void blockShouldSave() throws SQLException {
        //Given
        IpAddress ipAddress = new IpAddress("192.168.1.2", dataSource);
        LocalTime time = LocalTime.of(13, 0, 0);
        LocalDate date = LocalDate.of(2018, Month.JANUARY, 1);
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        when(dataSource.getConnection()).thenReturn(connection);
        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        when(connection.prepareStatement("INSERT INTO BLOCKED_IPS (IP,OCCURRENCES,DATE,REASON) VALUES (?,?,?,?)"))
        .thenReturn(preparedStatementMock);
        //When
        ipAddress.block(dateTime, Duration.ofHours(1), 150);
        //Then
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void equalsShouldCompareIps() {
        IpAddress ipAddres1 = new IpAddress("192.168.1.2", dataSource);
        IpAddress ipAddres2 = new IpAddress("192.168.1.2", dataSource);
        IpAddress ipAddres3 = new IpAddress("192.168.1.3", dataSource);

        assertThat(ipAddres1, is(ipAddres2));
        assertThat(ipAddres1, not(is(ipAddres3)));
    }

    @Test
    public void hashCodeShouldReturnIps() {
        IpAddress ipAddres1 = new IpAddress("192.168.1.2", dataSource);
        assertThat(ipAddres1.hashCode(), is("192.168.1.2".hashCode()));
    }
}