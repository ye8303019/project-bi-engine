package com.patsnap.insights.trickydata.endpoint.response;

import com.patsnap.common.entity.EntityObject;
import com.patsnap.insights.trickydata.endpoint.vo.DataCollectionVo;

import java.util.List;

public class DataCollectionListResponse extends EntityObject{
    private static final long serialVersionUID = 8558129319984365567L;
    private List<DataCollectionVo> data;

    public List<DataCollectionVo> getData() {
        return data;
    }

    public void setData(List<DataCollectionVo> data) {
        this.data = data;
    }
}
