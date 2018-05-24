package com.patsnap.insights.trickydata.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "chart_conf")
@IdClass(ChartConfigKey.class)
public class ChartConfigEntity {

    @Id
    private String name;

    @Id
    @Column(name = "dashboard_id")
    private String dashboardId;

    @Column(name = "dashboard_source")
    private String dashboardSource;

    private String dimension;

    private String measurement;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public String getDashboardSource() {
        return dashboardSource;
    }

    public void setDashboardSource(String dashboardSource) {
        this.dashboardSource = dashboardSource;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
