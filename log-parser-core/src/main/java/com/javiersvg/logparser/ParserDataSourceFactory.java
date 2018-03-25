package com.javiersvg.logparser;

import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;
import java.util.Properties;

class ParserDataSourceFactory implements DataSourceFactory {
    private Properties properties;

    ParserDataSourceFactory(Properties properties) {
        this.properties = properties;
    }

    public DataSource dataSource() {
        try {
            Reference dbReference = new Reference(properties.getProperty("dataSource"), properties.getProperty("dataSourceFactory"), null);
            dbReference.add(new StringRefAddr("port", properties.getProperty("port", "3306")));
            dbReference.add(new StringRefAddr("user", properties.getProperty("user")));
            dbReference.add(new StringRefAddr("password", properties.getProperty("password")));
            dbReference.add(new StringRefAddr("serverName", properties.getProperty("serverName","localhost")));
            dbReference.add(new StringRefAddr("databaseName", properties.getProperty("databaseName")));
            return (DataSource) NamingManager.getObjectInstance(dbReference, null, null, null);
        } catch (Exception e) {
            throw new LogParserException("Unable to load the properties file", e);
        }
    }
}
