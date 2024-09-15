package com.app.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface S3Service {

    boolean uploadFile(MultipartFile file, boolean isReadPublicly);

    InputStream downloadFileAsStream(String key);

    String createBucket(String bucketName);

    List<String> getBucketList();

    Map<String, String> listBucketsWithRegions();
}
