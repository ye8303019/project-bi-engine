package com.patsnap.insights.trickydata.endpoint.request;

import com.patsnap.common.entity.EntityObject;

public class DataCollectionQueryRequest extends EntityObject{
    private static final long serialVersionUID = -1684889762695694202L;

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
