package com.javiersvg.mysqllogparser;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.NamingManager;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Properties;

class MySqlLogParserDataSourceFactory implements DataSourceFactory {
    private Properties properties;

    MySqlLogParserDataSourceFactory(Properties properties) {
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
            throw new MySqlLogParserException("Unable to load the properties file", e);
        }
    }
}
