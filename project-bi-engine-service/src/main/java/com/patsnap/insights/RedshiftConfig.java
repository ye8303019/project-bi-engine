package com.patsnap.insights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by ye830 on 5/25/2018.
 */

@Configuration
public class RedshiftConfig {

    @Bean(name = "dsRedshift")
    @ConfigurationProperties(prefix="redshift.datasource")
    public DataSource redshiftDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcRedshift")
    @Autowired
    public JdbcTemplate redshiftJdbcTemplate(@Qualifier("dsRedshift") DataSource dsRedshift) {
        return new JdbcTemplate(dsRedshift);
    }
}
