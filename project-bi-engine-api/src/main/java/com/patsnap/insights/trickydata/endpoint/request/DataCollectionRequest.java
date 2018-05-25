package com.patsnap.insights.trickydata.endpoint.request;

import com.patsnap.common.entity.EntityObject;

import java.util.List;

public class DataCollectionRequest extends EntityObject{

    private static final long serialVersionUID = 6797957791218425611L;

    private String query;

    private int id;

    private String name;

    private List<String> tableList;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTableList() {
        return tableList;
    }

    public void setTableList(List<String> tableList) {
        this.tableList = tableList;
    }
}
