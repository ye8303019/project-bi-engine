package com.patsnap.insights.trickydata.endpoint;

import com.patsnap.insights.trickydata.endpoint.response.WorkChartListResponse;
import com.patsnap.insights.trickydata.endpoint.response.WorkChartResponse;

public interface WorkChartEndpoint {

    public WorkChartListResponse getWorkChartList();

    public WorkChartResponse createWorkChart();

    public WorkChartResponse saveWorkChart();

}
