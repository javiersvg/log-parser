package com.javiersvg.mysqllogparser;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class MySqlLogParserDataSourceFactoryTest {

    @Mock
    private Properties properties;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void dataSource() {
        when(properties.getProperty("dataSource")).thenReturn("com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource");
        when(properties.getProperty("dataSourceFactory")).thenReturn("com.mysql.jdbc.jdbc2.optional.MysqlDataSourceFactory");
        DataSource dataSource = new MySqlLogParserDataSourceFactory(properties).dataSource();
        assertThat(dataSource, is(MysqlConnectionPoolDataSource.class));
    }
}