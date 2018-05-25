package com.patsnap.insights.trickydata.endpoint.response;

import com.patsnap.common.entity.EntityObject;
import com.patsnap.insights.trickydata.endpoint.vo.DataSourceVo;

import java.util.List;

public class DataSourceListResponse extends EntityObject {

    private static final long serialVersionUID = 7088576290716003770L;
    private List<DataSourceVo> data;

    public List<DataSourceVo> getData() {
        return data;
    }

    public void setData(List<DataSourceVo> data) {
        this.data = data;
    }
}
