package com.patsnap.insights.trickydata.endpoint.request;

import com.patsnap.common.entity.EntityObject;

import java.util.List;

public class WorkChartRequest extends EntityObject {
    private static final long serialVersionUID = -3099838700007983929L;
    private int id;

    private List<String> dimensions;

    private List<String> measurements;

    private int dataCollectId;

    private String options;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDataCollectId() {
        return dataCollectId;
    }

    public void setDataCollectId(int dataCollectId) {
        this.dataCollectId = dataCollectId;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
