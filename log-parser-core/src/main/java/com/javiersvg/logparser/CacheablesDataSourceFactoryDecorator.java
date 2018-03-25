package com.javiersvg.logparser;

import javax.sql.DataSource;

class CacheablesDataSourceFactoryDecorator implements DataSourceFactory {
    private DataSource dataSource;
    private DataSourceFactory factory;

    CacheablesDataSourceFactoryDecorator(DataSourceFactory factory) {
        this.factory = factory;
    }

    @Override
    public DataSource dataSource() {
        if (dataSource == null) {
            dataSource = this.factory.dataSource();
        }
        return dataSource;
    }
}
