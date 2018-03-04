package com.javiersvg.mysqllogparser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class MySqlLogParserTest {
    private LocalTime time = LocalTime.of(0,0,0);
    private LocalDate date = LocalDate.of(2017,1,1);
    private LocalDateTime dateTime = LocalDateTime.of(date, time);
    @Mock
    private DataSourceFactory dataSourceFactory;
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @InjectMocks
    private MySqlLogParser parser = new MySqlLogParser(dateTime, Duration.ofHours(1), 100);

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        when(dataSourceFactory.dataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void parse() throws SQLException {
        //Given
        PreparedStatement preparedStatementMock = mock(PreparedStatement.class);
        when(connection.prepareStatement("INSERT INTO BLOCKED_IPS (IP,OCCURRENCES,DATE,REASON) VALUES (?,?,?,?)"))
                .thenReturn(preparedStatementMock);
        //When
        parser.parse();
        //Then
        verify(preparedStatementMock, times(2)).executeUpdate();
    }
}