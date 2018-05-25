package com.patsnap.insights;

import com.patsnap.insights.trickydata.endpoint.WorkChartEndpoint;
import com.patsnap.insights.trickydata.endpoint.response.WorkChartListResponse;
import com.patsnap.insights.trickydata.endpoint.response.WorkChartResponse;
import com.patsnap.insights.trickydata.entity.ChartConfigEntity;
import com.patsnap.insights.trickydata.manager.WorkChartManager;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkChartEndpointImpl implements WorkChartEndpoint{

    @Autowired
    WorkChartManager workChartManager;

    @Override
    public WorkChartListResponse getWorkChartList() {
        List<ChartConfigEntity> charts = workChartManager.getChartList(ContextHolder.USER_ID);
        Map<String, Map<String,List<String>>> chartDataList = new HashMap<>();
        for (ChartConfigEntity entity : charts) {
            chartDataList.put(entity.getId()+ "", workChartManager.getChartData(entity));
        }
        WorkChartListResponse response = new WorkChartListResponse();

        return response;
    }

    @Override
    public WorkChartResponse createWorkChart() {
        return null;
    }

    @Override
    public WorkChartResponse saveWorkChart() {
        return null;
    }
}
