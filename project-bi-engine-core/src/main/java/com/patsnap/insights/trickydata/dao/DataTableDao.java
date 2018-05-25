package com.patsnap.insights.trickydata.dao;

import com.patsnap.insights.trickydata.entity.DataTableEntity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataTableDao extends CrudRepository<DataTableEntity, Integer> {
    List<DataTableEntity> findAllByDatasourceIdEquals(Integer id);
}
