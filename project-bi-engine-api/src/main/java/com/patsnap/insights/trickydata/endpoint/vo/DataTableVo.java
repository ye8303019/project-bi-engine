package com.patsnap.insights.trickydata.endpoint.vo;

import com.patsnap.common.entity.EntityObject;

public class DataTableVo extends EntityObject{
    private static final long serialVersionUID = 1831866153358419081L;

    private String name;

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
