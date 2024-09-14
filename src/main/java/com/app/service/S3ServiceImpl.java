package com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Service
@Log4j2
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    @Override
    public boolean uploadFile(MultipartFile file, boolean isReadPublicly) {
        log.info("Started uploading file '{}' to S3 Bucket '{}'", file.getOriginalFilename(), bucketName);
        PutObjectRequest putObjectRequest;
        if (isReadPublicly) {
            putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file.getOriginalFilename()).acl("public-read")
                    .build();
        } else {
            putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file.getOriginalFilename())
                    .build();
        }
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            log.info("Successfully uploaded file to S3. Bucket: {}, Key: {}", bucketName, file.getOriginalFilename());
            return true;
        } catch (Exception e) {
            log.error("Failed to upload file to S3. Bucket: {}, Key: {}", bucketName, file.getOriginalFilename(), e);
            return false;
        }
    }

    @Override
    public InputStream downloadFileAsStream(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseBytes<GetObjectResponse> getObjectResponse = s3Client.getObjectAsBytes(getObjectRequest);
            if (getObjectResponse == null) {
                log.warn("Failed to get file from S3 bucket: Response is null");
                return new ByteArrayInputStream(new byte[0]);
            }

            log.info("Successfully getting file in bytes from S3 bucket.");
            byte[] fileBytes = getObjectResponse.asByteArray();
            return new ByteArrayInputStream(fileBytes);

        } catch (S3Exception e) {
            log.error("Failed to fetch object from S3 Bucket: {}, Key: {}", bucketName, key, e);
            throw e;
        } catch (SdkException e) {
            log.error("Error while downloading file from S3 Bucket: {}, Key: {}", bucketName, key, e);
            throw e;
        }
    }

    public String createBucket(String bucketName) {
        try {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            CreateBucketResponse createBucketResponse = s3Client.createBucket(createBucketRequest);
            return "Bucket created successfully: " + createBucketResponse.location();
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to create bucket: " + e.awsErrorDetails().errorMessage(), e);
        }
    }
}
