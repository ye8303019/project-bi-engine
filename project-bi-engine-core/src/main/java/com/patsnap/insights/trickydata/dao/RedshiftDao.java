package com.patsnap.insights.trickydata.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RedshiftDao {

    @Autowired
    //@Qualifier("jdbcRedshift")
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getData(String query) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from 20180525-company");
        return list;
    }

}
