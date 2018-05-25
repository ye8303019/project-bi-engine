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

import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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

    @Value("${config.com.patsnap.insights.bi.dimesions_dic}")
    public String DIMESION_DIC;

    private List<String> dimesionKey = Lists.newArrayList();

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

    //TODO find dimensions and measurements
    public DataCollectionResponse updateCollection(DataCollectionRequest request) {
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
        return response;
    }

    //TODO find dimensions and measurements
    public DataCollectionResponse createCollection(DataCollectionRequest request) {
        DataCollectionResponse response = new DataCollectionResponse();
        DataCollectionEntity entity = new DataCollectionEntity();

        //get data & upload
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
            Map<Integer, Integer> distanceMap = new HashMap<>();
            for (int i = 1; i < numbericValue.size(); i++) {
                Integer distance = Integer.valueOf(numbericValue.get(i).toString()) - Integer.valueOf(numbericValue.get(i - 1).toString());
                if (distanceMap.get(distance) == null) {
                    distanceMap.put(distance, 1);
                } else {
                    distanceMap.put(distance, distanceMap.get(distance) + 1);
                }
            }

            for (Map.Entry<Integer, Integer> entry : distanceMap.entrySet()) {
                if (entry.getValue().intValue() * 2 >=  numbericValue.size()) {
                    return BiType.DIMESION;
                }
            }
        }



        return BiType.MEASUREMENT;
    }


}
