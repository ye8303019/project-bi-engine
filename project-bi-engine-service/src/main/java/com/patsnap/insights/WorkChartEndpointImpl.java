package com.patsnap.insights;

import com.patsnap.insights.trickydata.endpoint.WorkChartEndpoint;
import com.patsnap.insights.trickydata.endpoint.request.WorkChartRequest;
import com.patsnap.insights.trickydata.endpoint.response.WorkChartListResponse;
import com.patsnap.insights.trickydata.endpoint.response.WorkChartResponse;
import com.patsnap.insights.trickydata.endpoint.vo.WorkChartVo;
import com.patsnap.insights.trickydata.entity.ChartConfigEntity;
import com.patsnap.insights.trickydata.manager.WorkChartManager;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/chart")
public class WorkChartEndpointImpl implements WorkChartEndpoint {

    @Autowired
    WorkChartManager workChartManager;

    @GetMapping(value = "/list")
    @Override
    public WorkChartListResponse getWorkChartList() {
        List<ChartConfigEntity> charts = workChartManager.getChartList(ContextHolder.USER_ID);
        List<WorkChartVo> data = Lists.newArrayList();
        for (ChartConfigEntity entity : charts) {
            data.add(workChartManager.convertToWorkChartVo(entity, workChartManager.getChartData(entity)));
        }
        WorkChartListResponse response = new WorkChartListResponse();
        response.setData(data);
        return response;
    }

    @PostMapping
    @Override
    public WorkChartResponse createWorkChart(@RequestBody WorkChartRequest request) {
        WorkChartResponse response = new WorkChartResponse();
        ChartConfigEntity entity = null;
        if (StringUtils.isEmpty(request.getId()) || !StringUtils.isNumeric(request.getId())) {
            entity = new ChartConfigEntity();
        }else {
            entity = workChartManager.getChartById(Integer.valueOf(request.getId()));
        }
        entity.setCollectionId(request.getDataCollectId());
        entity.setDimensions(StringUtils.join(request.getDimensions(), ","));
        entity.setMeasurements(StringUtils.join(request.getMeasurements(), ","));
        entity.setOptions(request.getOptions());
        entity.setUserId(ContextHolder.USER_ID);
        entity = workChartManager.saveChart(entity);
        response.setData(workChartManager.convertToWorkChartVo(entity, workChartManager.getChartData(entity)));
        return response;

    }

//    @PutMapping
//    @Override
//    public WorkChartResponse saveWorkChart(@RequestBody WorkChartRequest request) {
//        WorkChartResponse response = new WorkChartResponse();
//        ChartConfigEntity entity = workChartManager.getChartById(request.getId());
//        entity.setCollectionId(request.getDataCollectId());
//        entity.setDimensions(StringUtils.join(request.getDimensions(), ","));
//        entity.setMeasurements(StringUtils.join(request.getMeasurements(), ","));
//        entity.setOptions(request.getOptions());
//        entity.setUserId(ContextHolder.USER_ID);
//        entity = workChartManager.saveChart(entity);
//        response.setData(workChartManager.convertToWorkChartVo(entity, workChartManager.getChartData(entity)));
//        return response;
//    }

    @PostMapping(value = "/query")
    @Override
    public WorkChartResponse queryData(@RequestBody WorkChartRequest request) {
        WorkChartResponse response = new WorkChartResponse();
        ChartConfigEntity entity = new ChartConfigEntity();
        entity.setCollectionId(request.getDataCollectId());
        entity.setDimensions(StringUtils.join(request.getDimensions(), ","));
        entity.setMeasurements(StringUtils.join(request.getMeasurements(), ","));
        entity.setOptions(request.getOptions());
        response.setData(workChartManager.convertToWorkChartVo(entity, workChartManager.getChartData(entity)));
        return response;
    }
}
