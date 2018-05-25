package com.patsnap.insights.trickydata.endpoint;

import com.patsnap.insights.trickydata.endpoint.request.DataCollectionQueryRequest;
import com.patsnap.insights.trickydata.endpoint.request.DataCollectionRequest;
import com.patsnap.insights.trickydata.endpoint.response.DataCollectionListResponse;
import com.patsnap.insights.trickydata.endpoint.response.DataCollectionQueryResponse;
import com.patsnap.insights.trickydata.endpoint.response.DataCollectionResponse;

public interface DataCollectionEnpoint {
    public DataCollectionListResponse getDataCollections();

    public DataCollectionResponse getDataCollectionById(int id);

    public DataCollectionResponse updateDataCollection(DataCollectionRequest request);

    public DataCollectionQueryResponse queryData(DataCollectionQueryRequest request);

    public DataCollectionResponse createDataCollection(DataCollectionRequest request);
}
