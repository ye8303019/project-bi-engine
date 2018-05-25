package com.patsnap.insights.trickydata.endpoint.request;

import com.patsnap.common.entity.EntityObject;

/**
 * @Description:
 * @Author lip
 * @Date: 18-5-25 下午11:44
 */
public class ApiRequest extends EntityObject {
    private String url;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
