package com.patsnap.insights.trickydata.endpoint.response;

import com.patsnap.common.entity.EntityObject;

import java.util.List;
import java.util.Map;

public class DataCollectionQueryResponse extends EntityObject{
    private static final long serialVersionUID = 5685738798089462415L;

    private List<Map<String, String>> data;

    private List<String> title;

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }
}
