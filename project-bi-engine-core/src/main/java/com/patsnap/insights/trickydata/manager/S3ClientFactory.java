package com.patsnap.insights.trickydata.manager;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * @Description:
 * @Author lip
 * @Date: 18-5-25 下午12:06
 */
public class S3ClientFactory {
    private static final String key = "AKIAIKDL2LAVNJQJ7W7Q";
    private static final String secret = "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";
    private static AmazonS3 amazonS3;

    public static final String BUCKET_NAME = "bi.data";

    private void S3ClientFactory() {

    }

    public static AmazonS3 getClient() {
        if (amazonS3 == null) {
            amazonS3 = getAmazonS3();
        }
        return amazonS3;
    }

    private static AmazonS3 getAmazonS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(key, secret);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion("us-east-1").build();
    }
}
