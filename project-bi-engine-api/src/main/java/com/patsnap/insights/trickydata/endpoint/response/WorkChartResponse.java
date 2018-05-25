package com.patsnap.insights.trickydata.endpoint.response;

import com.patsnap.common.entity.EntityObject;
import com.patsnap.insights.trickydata.endpoint.vo.WorkChartVo;


public class WorkChartResponse extends EntityObject{
    private static final long serialVersionUID = -4682562430115259365L;

    private WorkChartVo data;

    public WorkChartVo getData() {
        return data;
    }

    public void setData(WorkChartVo data) {
        this.data = data;
    }
}
