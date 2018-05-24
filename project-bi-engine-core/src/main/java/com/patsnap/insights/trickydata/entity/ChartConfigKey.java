package com.patsnap.insights.trickydata.entity;

import java.io.Serializable;

import javax.persistence.Column;

public class ChartConfigKey implements Serializable {

    private String name;

    @Column(name = "dashboard_id")
    private String dashboardId;

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

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result + ((dashboardId == null) ? 0 : dashboardId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }

        final ChartConfigKey other = (ChartConfigKey)obj;
        if(name == null){
            if(other.name != null){
                return false;
            }
        }else if(!name.equals(other.name)){
            return false;
        }
        if(dashboardId == null){
            if(other.dashboardId != null){
                return false;
            }
        }else if(!dashboardId.equals(other.dashboardId)){
            return false;
        }
        return true;
    }
}
