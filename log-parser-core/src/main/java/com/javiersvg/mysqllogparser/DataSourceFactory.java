package com.javiersvg.mysqllogparser;

import javax.sql.DataSource;

interface DataSourceFactory {
    DataSource dataSource();
}
