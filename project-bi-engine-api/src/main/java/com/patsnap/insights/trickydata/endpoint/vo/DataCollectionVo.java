package com.patsnap.insights.trickydata.endpoint.vo;

import com.patsnap.common.entity.EntityObject;

import java.util.List;

public class DataCollectionVo extends EntityObject{

    private static final long serialVersionUID = -8892948965247473825L;

    private String id;

    private String name;

    private String sql;

    private List<DataTableVo> tables;

    private List<String> dimensions;

    private List<String> measurements;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<DataTableVo> getTables() {
        return tables;
    }

    public void setTables(List<DataTableVo> tables) {
        this.tables = tables;
    }

    public List<String> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<String> dimensions) {
        this.dimensions = dimensions;
    }

    public List<String> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<String> measurements) {
        this.measurements = measurements;
    }
}
