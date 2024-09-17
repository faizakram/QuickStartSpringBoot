package com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<String> getBucketList() throws RuntimeException {
        try {
            ListBucketsResponse listBucketsResponse = s3Client.listBuckets();
            return listBucketsResponse.buckets().stream()
                    .map(Bucket::name)
                    .toList();
        } catch (S3Exception e) {
            throw new RuntimeException("Failed to list buckets: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    @Override
    public Map<String, String> listBucketsWithRegions() {
        try {
            ListBucketsResponse listBucketsResponse = s3Client.listBuckets();

            // Create a map to store bucket names with their respective regions
            Map<String, String> bucketRegions = new HashMap<>();

            for (var bucket : listBucketsResponse.buckets()) {
                String bucketRegion = getBucketRegion(bucket.name());
                bucketRegions.put(bucket.name(), bucketRegion);
            }

            return bucketRegions;

        } catch (S3Exception e) {
            throw new RuntimeException("Failed to list buckets: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    private String getBucketRegion(String bucketName) {
        try {
            GetBucketLocationRequest locationRequest = GetBucketLocationRequest.builder()
                    .bucket(bucketName)
                    .build();

            GetBucketLocationResponse locationResponse = s3Client.getBucketLocation(locationRequest);

            // Translate the bucket location constraint to a region name
            Region region = locationResponse.locationConstraintAsString() == null ? Region.US_EAST_1 :
                    Region.of(locationResponse.locationConstraintAsString());

            return region.id();
        } catch (S3Exception e) {
            return "Unknown"; // Handle the case where the region is not accessible or available
        }
    }

}
