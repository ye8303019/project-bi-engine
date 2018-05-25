package com.patsnap.insights.trickydata.manager;

import com.patsnap.insights.ContextHolder;
import com.patsnap.insights.trickydata.dao.DataCollectionDao;
import com.patsnap.insights.trickydata.dao.RedshiftDao;
import com.patsnap.insights.trickydata.endpoint.request.DataCollectionRequest;
import com.patsnap.insights.trickydata.endpoint.response.DataCollectionListResponse;
import com.patsnap.insights.trickydata.endpoint.response.DataCollectionResponse;
import com.patsnap.insights.trickydata.endpoint.vo.DataCollectionVo;
import com.patsnap.insights.trickydata.endpoint.vo.DataTableVo;
import com.patsnap.insights.trickydata.entity.DataCollectionEntity;
import com.patsnap.insights.trickydata.entity.DataTableEntity;

import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataCollectionManager {

    @Autowired
    DataCollectionDao dataCollectionDao;

    @Autowired
    private DataTableManager dataTableManager;

    @Autowired
    private RedshiftDao redshiftDao;

    public DataCollectionListResponse getDataCollectionsByUserId(Integer userId) {
        DataCollectionListResponse response = new DataCollectionListResponse();
        List<DataCollectionEntity> dataCollectionEntityList = dataCollectionDao.findByUserId(userId);
        if (!CollectionUtils.isEmpty(dataCollectionEntityList)) {
            List<DataCollectionVo> dataCollectionVos = dataCollectionEntityList.stream().map(e -> convertToDataCollectionVo(e)).collect(Collectors.toList());
            response.setData(dataCollectionVos);
        }

        return response;
    }

    public DataCollectionResponse findDataCollectionsById(Integer id) {
        DataCollectionResponse response = new DataCollectionResponse();
        DataCollectionEntity dataCollectionEntities = dataCollectionDao.findOne(id);
        if (dataCollectionEntities != null) {
            response.setData(convertToDataCollectionVo(dataCollectionEntities));
        }
        return response;
    }

    public DataCollectionResponse updataCollection(DataCollectionRequest request) {
        DataCollectionResponse response = new DataCollectionResponse();
        DataCollectionEntity entity = dataCollectionDao.findOne(request.getId());
        entity.setDimensions("new dimensions " + new Date().getTime());
        entity.setMeasurements("new measurements " + new Date().getTime());
        entity.setName(request.getName());
        entity.setQuery(request.getQuery());
        entity.setTableList(StringUtils.join(request.getTableList(), ","));
        response.setData(convertToDataCollectionVo(dataCollectionDao.save(entity)));
        return response;
    }

    public DataCollectionResponse createCollection(DataCollectionRequest request) {
        DataCollectionResponse response = new DataCollectionResponse();
        DataCollectionEntity entity = new DataCollectionEntity();
        entity.setDimensions("new dimensions " + new Date().getTime());
        entity.setMeasurements("new measurements " + new Date().getTime());
        entity.setName(request.getName());
        entity.setQuery(request.getQuery());
        entity.setRemoteTableName(request.getName());
        entity.setTableList(StringUtils.join(request.getTableList(), ","));
        entity.setUserId(ContextHolder.USER_ID);
        entity = dataCollectionDao.save(entity);
        response.setData(convertToDataCollectionVo(entity));
        return response;
    }

    public List<Map<String, String>> queryData(String query) {
        List<Map<String, String>> result = Lists.newArrayList();
        Map<String, String> dataMap = null;
        List<Map<String, Object>> queryedData = redshiftDao.getData(query);
        for (Map<String, Object> map : queryedData) {
            dataMap = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                dataMap.put(entry.getKey(), entry.getValue().toString());
            }
            result.add(dataMap);
        }
        return result;
    }

    private DataCollectionVo convertToDataCollectionVo(DataCollectionEntity entity) {
        DataCollectionVo dataCollectionVo = new DataCollectionVo();
        if (null != entity) {
            dataCollectionVo.setDimensions(Arrays.asList(entity.getDimensions().split(",")));
            dataCollectionVo.setMeasurements(Arrays.asList(entity.getMeasurements().split(",")));
            dataCollectionVo.setId(entity.getId() + "");
            dataCollectionVo.setName(entity.getName());
            dataCollectionVo.setSql(entity.getQuery());
            List<String> ids = Arrays.asList(entity.getTableList().split(","));
            List<DataTableEntity> dataTableEntities = dataTableManager.getTablesByIds(ids.stream().map(id -> Integer.valueOf(id.trim())).collect(Collectors.toList()));
            dataCollectionVo.setTables(dataTableEntities.stream().filter(t -> t != null).map(e -> convertToDataTableVo(e)).collect(Collectors.toList()));
        }
        return dataCollectionVo;
    }

    private DataTableVo convertToDataTableVo(DataTableEntity entity) {
        DataTableVo dataTableVo = new DataTableVo();
        if (null != entity) {
            dataTableVo.setId(entity.getId() + "");
            dataTableVo.setName(entity.getName());
        }
        return dataTableVo;
    }
}
