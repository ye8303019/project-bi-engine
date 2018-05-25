package com.patsnap.insights.trickydata.manager;

import com.patsnap.insights.trickydata.dao.ChartConfigDao;
import com.patsnap.insights.trickydata.dao.DataCollectionDao;
import com.patsnap.insights.trickydata.dao.RedshiftDao;
import com.patsnap.insights.trickydata.endpoint.vo.WorkChartVo;
import com.patsnap.insights.trickydata.entity.ChartConfigEntity;
import com.patsnap.insights.trickydata.entity.DataCollectionEntity;

import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkChartManager {

    @Autowired
    ChartConfigDao chartConfigDao;

    @Autowired
    DataCollectionDao dataCollectionDao;

    @Autowired
    RedshiftDao redshiftDao;

    public List<ChartConfigEntity> getChartList(int userId) {
        return chartConfigDao.findByUserIdEquals(userId);
    }

    public ChartConfigEntity getChartById(int charId) {
        return chartConfigDao.findOne(charId);
    }

    public Map<String,List<String>> getChartData(ChartConfigEntity chart) {
        DataCollectionEntity dataCollectionEntity = dataCollectionDao.findOne(chart.getCollectionId());
        if (null != dataCollectionEntity) {
            List<String> demensions = Arrays.asList(chart.getDimensions().split(","));
            List<String> measurements = Arrays.asList(chart.getMeasurements().split(","));
            StringBuilder sb = new StringBuilder();
            sb.append("select ")
                    .append(StringUtils.join(demensions, ","))
                    .append(" , ")
                    .append(StringUtils.join(measurements, ","))
                    .append(" from ")
                    .append(dataCollectionEntity.getRemoteTableName());
            List<Map<String, Object>> data = redshiftDao.getData(sb.toString());
            if (!CollectionUtils.isEmpty(data)){
                Map<String,List<String>> result = new HashMap<>();
                for (Map<String, Object> map : data) {
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        if (null == result.get(entry.getKey())) {
                            result.put(entry.getKey(), Lists.newArrayList());
                        }
                        result.get(entry.getKey()).add(entry.getValue().toString());
                    }
                }
                return result;
            }
        }

        return new HashMap<>();
    }

    public ChartConfigEntity saveChart(ChartConfigEntity chart){
        chart = chartConfigDao.save(chart);
        return chart;
    }

    public WorkChartVo convertToWorkChartVo(ChartConfigEntity entity, Map<String,List<String>> map) {
        WorkChartVo workChartVo = new WorkChartVo();
        workChartVo.setId(entity.getId() + "");
        workChartVo.setCollectionId(entity.getCollectionId() + "");
        workChartVo.setData(map);
        workChartVo.setDimesions(Arrays.asList(entity.getDimensions().split(",")));
        workChartVo.setMeasurements(Arrays.asList(entity.getMeasurements().split(",")));
        workChartVo.setOptions(entity.getOptions());
        return workChartVo;
    }


}
