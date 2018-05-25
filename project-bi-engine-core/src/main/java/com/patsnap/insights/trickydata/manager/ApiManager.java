package com.patsnap.insights.trickydata.manager;

import com.patsnap.insights.ContextHolder;
import com.patsnap.insights.trickydata.dao.RedshiftDao;
import com.patsnap.insights.trickydata.endpoint.request.ApiRequest;
import com.patsnap.insights.trickydata.entity.DataSourceEntity;
import com.patsnap.insights.trickydata.entity.DataTableEntity;

import com.amazonaws.util.json.Jackson;
import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    RedshiftDao redshiftDao;

    public String generalJsonFile(ApiRequest apiRequest) {
        List<Object> jsonObject = getJsonFromApi(apiRequest.getUrl());
        List<String> strList = jsonObject.stream().map(e -> Jackson.toJsonString(e)).collect(Collectors.toList());
        String jsonString = StringUtils.join(strList, "");

        JsonObject jsonObject1 = (JsonObject) new Gson().toJsonTree(jsonObject.get(0));

        List<String> titles = jsonObject1.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());

        String s3Key = s3Manager.putObject(apiRequest.getName(), MediaType.JSON_UTF_8, jsonString.getBytes());

        String schema = StringUtils.join(titles, " VARCHAR(100) NOT NULL, ");
        schema = "CREATE TABLE \"" + s3Key + "\" (" + schema + " VARCHAR(100) NOT NULL);";
        redshiftDao.createTable(schema);
        redshiftDao.copy(s3Key, s3Key);

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

    private List<Object> getJsonFromApi(String url) {
        RestTemplate restTemplate = new RestTemplate();
        List<Object> response = restTemplate.getForObject("http://127.0.0.1:3000", ArrayList.class);

        return response;
    }
}
