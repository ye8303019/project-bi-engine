package com.patsnap.insights.trickydata.dao;

import com.patsnap.insights.trickydata.entity.DataCollectionEntity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DataCollectionDao extends CrudRepository<DataCollectionEntity, Integer>{
    public List<DataCollectionEntity> findByUserId(Integer userId);
}
