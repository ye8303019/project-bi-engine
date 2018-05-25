package com.patsnap.insights.trickydata.manager;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.google.common.net.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Date;


/**
 * @Description:
 * @Author lip
 * @Date: 18-5-25 下午12:31
 */
@Service
public class S3Manager {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Manager.class);
    private AmazonS3 amazonS3 = S3ClientFactory.getClient();

    public String putObject(String fileName, MediaType mediaType, byte[] bytes) {
        LOGGER.info("upload s3 file start");
        String s3Key = generateS3Key(fileName);
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bais.available());
            metadata.setContentType(mediaType.toString());
            metadata.setHeader(Headers.STORAGE_CLASS, StorageClass.ReducedRedundancy.toString());
            PutObjectRequest putRequest = new PutObjectRequest(S3ClientFactory.BUCKET_NAME, s3Key, bais, metadata);
            amazonS3.putObject(putRequest);
            LOGGER.info("upload s3 file end");
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            throw e;
        }
        return s3Key;
    }

    private String generateS3Key(String fileName) {
        StringBuilder builder = new StringBuilder(256);
        builder.append(new Date().getTime()).append(fileName);
        return builder.toString();
    }
}
