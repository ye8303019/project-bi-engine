package com.patsnap.insights.trickydata.manager;

import com.patsnap.common.utils.UniqueString;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.google.common.net.MediaType;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;


/**
 * @Description:
 * @Author lip
 * @Date: 18-5-25 下午12:31
 */
@Service
public class S3Manager {
    private AmazonS3 amazonS3 = S3ClientFactory.getClient();

    public String putObject(String fileName, MediaType mediaType, byte[] bytes) {
        String s3Key = generateS3Key(fileName);
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bais.available());
            metadata.setContentType(mediaType.toString());
            metadata.setHeader(Headers.STORAGE_CLASS, StorageClass.ReducedRedundancy.toString());
            PutObjectRequest putRequest = new PutObjectRequest(S3ClientFactory.BUCKET_NAME, s3Key, bais, metadata);
            amazonS3.putObject(putRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s3Key;
    }

    private String generateS3Key(String fileName) {
        StringBuilder builder = new StringBuilder(256);
        builder.append(UniqueString.uuidUniqueString())
                .append("/").append(fileName);
        return builder.toString();
    }
}
