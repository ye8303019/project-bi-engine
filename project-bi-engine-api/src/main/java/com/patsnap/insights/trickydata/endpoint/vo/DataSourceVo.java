package com.patsnap.insights.trickydata.endpoint.vo;

import com.patsnap.common.entity.EntityObject;

import java.util.List;

public class DataSourceVo extends EntityObject{
    private static final long serialVersionUID = 6965513356881482620L;

    private String name;

    private String id;

    private List<DataTableVo> tables;

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

    public List<DataTableVo> getTables() {
        return tables;
    }

    public void setTables(List<DataTableVo> tables) {
        this.tables = tables;
    }
}
