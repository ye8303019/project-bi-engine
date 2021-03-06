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
import com.patsnap.insights.trickydata.type.BiType;

import com.amazonaws.util.json.Jackson;
import com.google.common.collect.Lists;
import com.google.common.net.MediaType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DataCollectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataCollectionManager.class);

    @Autowired
    DataCollectionDao dataCollectionDao;

    @Autowired
    S3Manager s3Manager;

    @Autowired
    private DataTableManager dataTableManager;

    @Autowired
    private RedshiftDao redshiftDao;

    @Value("${config.com.patsnap.insights.bi.dimesions_dic}")
    public String DIMESION_DIC;

    private List<String> dimesionKey = Lists.newArrayList();

    public DataCollectionListResponse getDataCollectionsByUserId(Integer userId) {
        LOGGER.info("start to get data collection by user id {}", userId);
        DataCollectionListResponse response = new DataCollectionListResponse();
        List<DataCollectionEntity> dataCollectionEntityList = dataCollectionDao.findByUserId(userId);
        if (!CollectionUtils.isEmpty(dataCollectionEntityList)) {
            List<DataCollectionVo> dataCollectionVos = dataCollectionEntityList.stream().map(e -> convertToDataCollectionVo(e)).collect(Collectors.toList());
            response.setData(dataCollectionVos);
        }
        LOGGER.info("end to get data collection by user id {}, data {}.", userId, response.getData());
        return response;
    }

    public DataCollectionResponse findDataCollectionsById(Integer id) {
        LOGGER.info("start to find data collection by id {}", id);
        DataCollectionResponse response = new DataCollectionResponse();
        DataCollectionEntity dataCollectionEntities = dataCollectionDao.findOne(id);
        if (dataCollectionEntities != null) {
            response.setData(convertToDataCollectionVo(dataCollectionEntities));
        }
        LOGGER.info("end to find data collection by id {}, data {}.", id, response.getData());
        return response;
    }

    public DataCollectionResponse updateCollection(DataCollectionRequest request) {
        LOGGER.info("start to update data collection {}", request);
        DataCollectionResponse response = new DataCollectionResponse();
        DataCollectionEntity entity = dataCollectionDao.findOne(request.getId());
        if (!entity.getQuery().toLowerCase().equals(request.getQuery().toLowerCase())) {
            List<Map<String, Object>> queryedData = redshiftDao.getData(request.getQuery());
            List<String> dimensions = Lists.newArrayList();
            List<String> measurements = Lists.newArrayList();
            Map<String, List<Object>> result = new HashMap<>();
            if (!CollectionUtils.isEmpty(queryedData)) {
                for (Map<String, Object> map : queryedData) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        if (null == result.get(entry.getKey())) {
                            result.put(entry.getKey(), Lists.newArrayList());
                        }
                        result.get(entry.getKey()).add(entry.getValue());
                    }
                }

                for (Map.Entry<String, List<Object>> entry : result.entrySet()){
                    if (judgeColumn(entry.getKey(), entry.getValue()).equals(BiType.MEASUREMENT)) {
                        measurements.add(entry.getKey());
                    }else{
                        dimensions.add(entry.getKey());
                    }
                }
            }
            entity.setDimensions(dimensions.isEmpty() ? result.keySet().iterator().next() : StringUtils.join(dimensions, ","));
            entity.setMeasurements(measurements.isEmpty() ? result.keySet().iterator().next() : StringUtils.join(measurements, ","));
            entity.setQuery(request.getQuery());
        }
        entity.setName(request.getName());
        entity.setTableList(StringUtils.join(request.getTableList(), ","));
        response.setData(convertToDataCollectionVo(dataCollectionDao.save(entity)));
        LOGGER.info("end to update data collection {}", response.getData());
        return response;
    }

    //TODO find dimensions and measurements
    public DataCollectionResponse createCollection(DataCollectionRequest request) {
        LOGGER.info("start to create data collection {}", request);
        DataCollectionResponse response = new DataCollectionResponse();
        DataCollectionEntity entity = new DataCollectionEntity();

        //get data & upload
        List<Map<String, Object>> queryedData = redshiftDao.getData(request.getQuery());

        List<String> strList = queryedData.stream().map(e -> {
            e.put("id", UUID.randomUUID().toString());
            return Jackson.toJsonString(e);
        }).collect(Collectors.toList());
        String jsonString = StringUtils.join(strList, "");

        String s3Key = s3Manager.putObject(request.getName(), MediaType.JSON_UTF_8, jsonString.getBytes());
        Set<String> keySet = queryedData.get(0).keySet();
        keySet.remove("id");
        String schema = StringUtils.join(keySet, " VARCHAR(100) NOT NULL, ");
        schema = "CREATE TABLE \"" + s3Key + "\" (id VARCHAR(100) NOT NULL primary key, " + schema + " VARCHAR(100) NOT NULL);";
        redshiftDao.createTable(schema);
        redshiftDao.copy(s3Key, s3Key);

        List<String> dimensions = Lists.newArrayList();
        List<String> measurements = Lists.newArrayList();
        Map<String, List<Object>> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(queryedData)) {
            for (Map<String, Object> map : queryedData) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (!entry.getKey().equals("id")) {
                        if (null == result.get(entry.getKey())) {
                            result.put(entry.getKey(), Lists.newArrayList());
                        }
                        result.get(entry.getKey()).add(entry.getValue());
                    }
                }
            }

            for (Map.Entry<String, List<Object>> entry : result.entrySet()){
                if (judgeColumn(entry.getKey(), entry.getValue()).equals(BiType.MEASUREMENT)) {
                    measurements.add(entry.getKey());
                }else{
                    dimensions.add(entry.getKey());
                }
            }
        }
        entity.setDimensions(dimensions.isEmpty() ? result.keySet().iterator().next() : StringUtils.join(dimensions, ","));
        entity.setMeasurements(measurements.isEmpty() ? result.keySet().iterator().next() : StringUtils.join(measurements, ","));
        entity.setName(request.getName());
        entity.setQuery(request.getQuery());
        entity.setRemoteTableName(s3Key); // todo table name
        entity.setTableList(StringUtils.join(request.getTableList(), ","));
        entity.setUserId(ContextHolder.USER_ID);
        entity = dataCollectionDao.save(entity);
        response.setData(convertToDataCollectionVo(entity));
        LOGGER.info("end to create data collection {}", response.getData());
        return response;
    }

    public List<Map<String, String>> queryData(String query) {
        List<Map<String, String>> result = Lists.newArrayList();
        Map<String, String> dataMap = null;
        List<Map<String, Object>> queryedData = redshiftDao.getData(query);
        for (Map<String, Object> map : queryedData) {
            dataMap = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (!entry.getKey().equals("id")) {
                    dataMap.put(entry.getKey(), entry.getValue().toString());
                }
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


    public BiType judgeColumn(String name, List<Object> value) {
        if (CollectionUtils.isEmpty(dimesionKey)) {
            dimesionKey = Arrays.asList(DIMESION_DIC.split("|"));
        }
        if (dimesionKey.contains(name.toLowerCase())) {
            return BiType.DIMESION;
        }

        int size = value.size();
        //judge if value continuity
        if ( size <= 1) {
            return BiType.MEASUREMENT;
        }

        List numbericValue = value.stream().filter(o -> StringUtils.isNumeric(o.toString())).collect(Collectors.toList());
        if (numbericValue.size() * 2 > size) {
            Map<String, Integer> distanceMap = new HashMap<>();
            for (int i = 1; i < numbericValue.size(); i++) {
                String distance = (new BigDecimal(numbericValue.get(i).toString())).subtract(new BigDecimal(numbericValue.get(i - 1).toString())).toString();
                if (distanceMap.get(distance) == null) {
                    distanceMap.put(distance, 1);
                } else {
                    distanceMap.put(distance, distanceMap.get(distance) + 1);
                }
            }

            for (Map.Entry<String, Integer> entry : distanceMap.entrySet()) {
                if (entry.getValue().intValue() * 2 >=  numbericValue.size()) {
                    return BiType.DIMESION;
                }
            }
        }
        return BiType.MEASUREMENT;
    }
}
