package com.patsnap.insights.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:config/application.properties"})
public class Constant {

    @Value("${config.com.patsnap.insights.bi.dimesions_dic}")
    public String DIMESION_DIC;
}


