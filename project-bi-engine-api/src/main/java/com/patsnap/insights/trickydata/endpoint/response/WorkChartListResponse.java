package com.patsnap.insights.trickydata.endpoint.response;

import com.patsnap.common.entity.EntityObject;
import com.patsnap.insights.trickydata.endpoint.vo.WorkChartVo;

import java.util.List;

public class WorkChartListResponse extends EntityObject{
    private static final long serialVersionUID = 1365966191559044987L;

    List<WorkChartVo> data;

    public List<WorkChartVo> getData() {
        return data;
    }

    public void setData(List<WorkChartVo> data) {
        this.data = data;
    }
}
