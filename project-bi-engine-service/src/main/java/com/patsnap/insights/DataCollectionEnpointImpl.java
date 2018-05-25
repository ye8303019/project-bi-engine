package com.patsnap.insights;

import com.patsnap.insights.trickydata.endpoint.DataCollectionEnpoint;
import com.patsnap.insights.trickydata.endpoint.request.DataCollectionQueryRequest;
import com.patsnap.insights.trickydata.endpoint.request.DataCollectionRequest;
import com.patsnap.insights.trickydata.endpoint.response.DataCollectionListResponse;
import com.patsnap.insights.trickydata.endpoint.response.DataCollectionQueryResponse;
import com.patsnap.insights.trickydata.endpoint.response.DataCollectionResponse;
import com.patsnap.insights.trickydata.manager.DataCollectionManager;

import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/data/collection")
public class DataCollectionEnpointImpl implements DataCollectionEnpoint{

    @Autowired
    private DataCollectionManager dataCollectionManager;


    @GetMapping(value = "/list")
    @Override
    public DataCollectionListResponse getDataCollections() {
        DataCollectionListResponse response = dataCollectionManager.getDataCollectionsByUserId(1);
        return response;
    }

    @GetMapping(value = "/{id}")
    @Override
    public DataCollectionResponse getDataCollectionById(@PathVariable int id) {
        DataCollectionResponse response = dataCollectionManager.findDataCollectionsById(id);
        return response;
    }

    @PutMapping
    @Override
    public DataCollectionResponse updateDataCollection(@RequestBody DataCollectionRequest request) {
        DataCollectionResponse response = dataCollectionManager.updateCollection(request);
        return response;
    }

    @PostMapping(value = "/query")
    @Override
    public DataCollectionQueryResponse queryData(@RequestBody DataCollectionQueryRequest request) {
        DataCollectionQueryResponse response = new DataCollectionQueryResponse();
        response.setData(dataCollectionManager.queryData(request.getQuery()));
        if (!CollectionUtils.isEmpty(response.getData())) {
            response.setTitle(Lists.newArrayList(response.getData().get(0).keySet()));
        }
        return response;
    }

    @PostMapping
    @Override
    public DataCollectionResponse createDataCollection(@RequestBody DataCollectionRequest request) {
        DataCollectionResponse response = dataCollectionManager.createCollection(request);
        return response;
    }


}
