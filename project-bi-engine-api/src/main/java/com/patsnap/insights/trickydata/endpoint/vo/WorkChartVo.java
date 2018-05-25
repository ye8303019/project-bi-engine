package com.patsnap.insights.trickydata.endpoint.vo;

import com.patsnap.common.entity.EntityObject;

import java.util.List;
import java.util.Map;

public class WorkChartVo extends EntityObject {

    private static final long serialVersionUID = -7313426310239633564L;

    private String id;

    private String collectionId;

    private List<String> dimesions;

    private List<String> measurements;

    private String options;

    private Map<String, List<String>> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public List<String> getDimesions() {
        return dimesions;
    }

    public void setDimesions(List<String> dimesions) {
        this.dimesions = dimesions;
    }

    public List<String> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<String> measurements) {
        this.measurements = measurements;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Map<String, List<String>> getData() {
        return data;
    }

    public void setData(Map<String, List<String>> data) {
        this.data = data;
    }
}
