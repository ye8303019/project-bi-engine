package com.patsnap.insights.trickydata.dao;

import com.patsnap.insights.trickydata.entity.UserEntity;

import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<UserEntity, Integer> {
    public UserEntity getUserByName(String name);
}
