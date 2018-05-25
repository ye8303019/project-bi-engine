package com.patsnap.insights.trickydata.manager;

import com.patsnap.insights.ContextHolder;
import com.patsnap.insights.trickydata.dao.DataSourceDao;
import com.patsnap.insights.trickydata.endpoint.response.DataSourceListResponse;
import com.patsnap.insights.trickydata.endpoint.vo.DataSourceVo;
import com.patsnap.insights.trickydata.endpoint.vo.DataTableVo;
import com.patsnap.insights.trickydata.entity.DataSourceEntity;
import com.patsnap.insights.trickydata.entity.DataTableEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataSourceManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceManager.class);

    @Autowired
    DataSourceDao dataSourceDao;

    @Autowired
    private DataTableManager dataTableManager;

    public DataSourceListResponse getDataSourceList(Integer id) {
        LOGGER.info("start to get data source list by user id {}", id);
        DataSourceListResponse response = new DataSourceListResponse();
        response.setData(dataSourceDao.findAllByUserIdEquals(id).stream().filter(e -> e != null).map(e -> convertToDataSourceVo(e)).collect(Collectors.toList()));
        LOGGER.info("end to get data source list by user id {}, data {}", id, response.getData());
        return response;
    }

    public DataSourceEntity saveDataSource(DataSourceEntity entity) {
        LOGGER.info("start to save data source {}", entity);
        if (null != entity) {
            entity = dataSourceDao.save(entity);
        }
        return entity;
    }

    private DataSourceVo convertToDataSourceVo(DataSourceEntity entity) {
        DataSourceVo dataSourceVo = new DataSourceVo();
        if (null != entity) {
            dataSourceVo.setId(entity.getId() + "");
            dataSourceVo.setName(entity.getName());
            List<DataTableEntity> tables = dataTableManager.getTablesByDataSourceId(entity.getId());
            dataSourceVo.setTables(tables.stream().filter(e -> e != null).map(e -> convertToDataTableVo(e)).collect(Collectors.toList()));
        }
        return dataSourceVo;
    }

    private DataTableVo convertToDataTableVo(DataTableEntity entity) {
        DataTableVo dataTableVo = new DataTableVo();
        if (null != entity){
            dataTableVo.setId(entity.getId() + "");
            dataTableVo.setName(entity.getName());
        }
        return dataTableVo;
    }
}
