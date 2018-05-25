package com.patsnap.insights.trickydata.dao;

import com.patsnap.insights.trickydata.entity.DataCollectionEntity;
import com.patsnap.insights.trickydata.entity.DataSourceEntity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataSourceDao extends CrudRepository<DataSourceEntity, Integer> {

    List<DataSourceEntity> findAllByUserIdEquals(int id);
}
