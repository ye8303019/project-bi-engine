package com.patsnap.insights.trickydata.dao;

import com.patsnap.insights.trickydata.entity.ChartConfigEntity;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChartConfigDao extends CrudRepository<ChartConfigEntity, Integer> {
    public List<ChartConfigEntity> findByUserIdEquals(Integer userId);
}
