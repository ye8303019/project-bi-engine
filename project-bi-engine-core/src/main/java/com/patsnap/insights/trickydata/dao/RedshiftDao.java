package com.patsnap.insights.trickydata.dao;

import com.patsnap.insights.trickydata.manager.DataTableManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RedshiftDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataTableManager.class);
    private static final String key = "AKIAIKDL2LAVNJQJ7W7Q";
    private static final String secret = "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";

    @Autowired
    @Qualifier("jdbcRedshift")
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getData(String query) {
        LOGGER.info("query redshift {}", query);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(query);
        LOGGER.info("query redshift end");
        return list;
    }

    public boolean copy(String tableName, String fileName) {
        try {
            String sql = "copy \"" + tableName + "\" from 's3://bi.data/" + fileName + "' \n" +
                    "credentials 'aws_iam_role=arn:aws:iam::988868554680:role/myRedShiftToS3' \n" +
                    "json 'auto' region 'us-east-1';";

            LOGGER.info("copy tableName from s3 {}", sql);

            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            LOGGER.error("copy error", e);
            return false;
        }
        return true;
    }


    public void createTable(String tableSchema) {
        LOGGER.info("create redshift table {} begin", tableSchema);
        jdbcTemplate.execute(tableSchema);
        LOGGER.info("create redshift end");
    }

}
