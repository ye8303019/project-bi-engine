package com.patsnap.insights.trickydata.type;

public enum DataSourceType {
    FILE("file"),
    API("api");
    private String value;

    DataSourceType(String value) {
        this.value = value;
    }
}
