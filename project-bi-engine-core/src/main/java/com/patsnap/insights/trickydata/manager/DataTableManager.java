package com.patsnap.insights.trickydata.manager;

import com.patsnap.insights.trickydata.dao.DataTableDao;
import com.patsnap.insights.trickydata.entity.DataTableEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataTableManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataTableManager.class);

    @Autowired
    DataTableDao dataTableDao;

    public DataTableEntity getTableById(Integer tableId) {
        LOGGER.info("start to get data table by table id {}", tableId);
        DataTableEntity dataTableEntity = dataTableDao.findOne(tableId);
        LOGGER.info("end to get data table by table id {}, data {}", dataTableEntity);
        return dataTableEntity;
    }

    public List<DataTableEntity> getTablesByIds(List<Integer> ids) {
        LOGGER.info("start to get data tables by table ids {}", ids);
        List<DataTableEntity> list = ids.stream().map(id -> getTableById(Integer.valueOf(id))).collect(Collectors.toList());
        LOGGER.info("end to get data tables by table ids {}, data {}", list);
        return list;
    }

    public List<DataTableEntity> getTablesByDataSourceId(Integer dsId) {
        LOGGER.info("start to get data tables by data source id {}", dsId);
        List<DataTableEntity> list = dataTableDao.findAllByDatasourceIdEquals(dsId);
        LOGGER.info("end to get data tables by data source id {}, data {}", dsId, list);
        return list;
    }

    public List<DataTableEntity> saveDataTables(List<DataTableEntity> entities) {
        LOGGER.info("start to save data tables {}", entities);
        Iterable<DataTableEntity> result = dataTableDao.save(entities);
        LOGGER.info("end to save data tables {}", entities);
        List<DataTableEntity> list = new ArrayList<>();
        list.forEach(e -> list.add(e));
        return list;
    }

    public DataTableEntity saveDataTable(DataTableEntity entity) {
        return dataTableDao.save(entity);
    }
}

