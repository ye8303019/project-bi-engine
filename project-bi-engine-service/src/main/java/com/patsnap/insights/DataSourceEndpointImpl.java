package com.patsnap.insights;

import com.patsnap.insights.trickydata.dao.RedshiftDao;
import com.patsnap.insights.trickydata.endpoint.DataSourceEnpoint;
import com.patsnap.insights.trickydata.endpoint.response.DataSourceListResponse;
import com.patsnap.insights.trickydata.manager.DataSourceManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import javax.naming.Context;

@RestController
@RequestMapping(value = "/data/source")
public class DataSourceEndpointImpl implements DataSourceEnpoint{

    @Autowired
    DataSourceManager dataSourceManager;

    @GetMapping
    @Override
    public DataSourceListResponse getDataSourceList() {
        return dataSourceManager.getDataSourceList(ContextHolder.USER_ID);
    }

}
