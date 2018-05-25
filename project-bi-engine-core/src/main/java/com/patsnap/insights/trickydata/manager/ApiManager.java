package com.patsnap.insights.trickydata.manager;

import com.google.common.net.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author lip
 * @Date: 18-5-25 下午12:53
 */
@Service
public class ApiManager extends BaseManager {
    @Autowired
    S3Manager s3Manager;
    @Autowired
    RedShiftFactory redShiftFactory;

    public String generalJsonFile(String url) {
        String fileName = "";
        String jsonString = getJsonFromApi(url);
        String s3Key = s3Manager.putObject(fileName, MediaType.OOXML_SHEET, jsonString.getBytes());

        return s3Key;
    }

    public void test() {
        redShiftFactory.getInstance();
    }

    private String getJsonFromApi(String url) {
        return null;
    }
}
