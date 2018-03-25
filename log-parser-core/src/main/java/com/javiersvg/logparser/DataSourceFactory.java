package com.javiersvg.logparser;

import javax.sql.DataSource;

interface DataSourceFactory {
    DataSource dataSource();
}
