package com.patsnap.insights.trickydata.endpoint.response;

import com.patsnap.common.entity.EntityObject;
import com.patsnap.insights.trickydata.endpoint.vo.DataCollectionVo;

public class DataCollectionResponse extends EntityObject{
    private static final long serialVersionUID = -489431215654510462L;

    private DataCollectionVo data;

    public DataCollectionVo getData() {
        return data;
    }

    public void setData(DataCollectionVo data) {
        this.data = data;
    }
}
