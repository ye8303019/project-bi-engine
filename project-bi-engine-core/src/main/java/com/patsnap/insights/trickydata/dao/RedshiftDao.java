package com.patsnap.insights.trickydata.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RedshiftDao {

    private static final String key = "AKIAIKDL2LAVNJQJ7W7Q";
    private static final String secret = "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";

    @Autowired
    @Qualifier("jdbcRedshift")
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getData(String query) {
        StringUtils.replaceAll(query, "\"", "\\\"");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(query);
        return list;
    }

    public boolean copy(String fileName) {
        try {
            jdbcTemplate.execute("copy " + fileName + " from s3://bi.data"
                    + " access_key_id " + key
                    + " secret_access_key " + secret);
        } catch (Exception e) {
            System.out.print(e.getStackTrace());
            return false;
        }
        return true;
    }

}
