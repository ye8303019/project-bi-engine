package com.patsnap.insights.trickydata.endpoint;

import com.patsnap.insights.trickydata.endpoint.request.WorkChartRequest;
import com.patsnap.insights.trickydata.endpoint.response.WorkChartListResponse;
import com.patsnap.insights.trickydata.endpoint.response.WorkChartResponse;

public interface WorkChartEndpoint {

    public WorkChartListResponse getWorkChartList();

    public WorkChartResponse createWorkChart(WorkChartRequest request);

    public WorkChartResponse saveWorkChart(WorkChartRequest request);

    public WorkChartResponse queryData(WorkChartRequest request);
}
