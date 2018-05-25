package com.patsnap.insights.trickydata.manager;

import com.patsnap.insights.ContextHolder;
import com.patsnap.insights.trickydata.entity.DataSourceEntity;
import com.patsnap.insights.trickydata.entity.DataTableEntity;
import com.patsnap.insights.trickydata.entrydata.YearNumberEntity;

import com.amazonaws.util.json.Jackson;
import com.google.common.net.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author lip
 * @Date: 18-5-25 下午12:53
 */
@Service
public class ApiManager extends BaseManager {
    @Autowired
    S3Manager s3Manager;
    @Autowired
    RedShiftFactory redShiftFactory;
    @Autowired
    DataTableManager tableManager;

    @Autowired
    DataSourceManager dataSourceManager;

    public String generalJsonFile(String url, String fileName) {
        List<YearNumberEntity> jsonObject = getJsonFromApi(url);
        String jsonString = Jackson.toJsonString(jsonObject);
        String s3Key = s3Manager.putObject(fileName + ".json", MediaType.JSON_UTF_8, jsonString.getBytes());

        DataSourceEntity dataSourceEntity = new DataSourceEntity();
        dataSourceEntity.setName(s3Key);
        dataSourceEntity.setUserId(ContextHolder.USER_ID);
        dataSourceEntity = dataSourceManager.saveDataSource(dataSourceEntity);

        DataTableEntity dataTableEntity = new DataTableEntity();
        dataTableEntity.setDatasourceId(dataSourceEntity.getId());
        // todo check table name
        dataTableEntity.setName(s3Key);
        tableManager.saveDataTable(dataTableEntity);
        return s3Key;
    }

    public void test() {
        redShiftFactory.getInstance();
    }

    private List<YearNumberEntity> getJsonFromApi(String url) {
        RestTemplate restTemplate = new RestTemplate();
        List<YearNumberEntity> response = restTemplate.getForObject("http://127.0.0.1:3000", ArrayList.class);

        return response;
    }
}
