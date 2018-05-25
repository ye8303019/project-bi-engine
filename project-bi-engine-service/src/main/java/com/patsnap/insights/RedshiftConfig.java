package com.patsnap.insights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by ye830 on 5/25/2018.
 */

@Configuration
@EnableConfigurationProperties
@Order
public class RedshiftConfig {

    @Bean(name = "dsMySql")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource mySqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplate")
    @Autowired
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier("dsMySql") DataSource dsMySql) {
        return new JdbcTemplate(dsMySql);
    }

    @Bean(name = "dsRedshift")
    @ConfigurationProperties(prefix = "redshift.datasource")
    public DataSource redshiftDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcRedshift")
    @Autowired
    public JdbcTemplate redshiftJdbcTemplate(@Qualifier("dsRedshift") DataSource dsRedshift) {
        return new JdbcTemplate(dsRedshift);
    }
}
