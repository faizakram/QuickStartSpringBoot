package com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Log4j2
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;
    private static final int THREAD_POOL_SIZE = 10; // Number of threads for concurrent execution


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

    @Override
    public void streamAllFilesAsZip(String bucketName, ZipOutputStream zos) {
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response listObjectsResponse;
        do {
            listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);
            List<S3Object> objects = listObjectsResponse.contents();

            for (S3Object object : objects) {
                addFileToZipStream(bucketName, object.key(), zos);
            }

        } while (listObjectsResponse.isTruncated());
    }

    private void addFileToZipStream(String bucketName, String keyName, ZipOutputStream zos) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        try (ResponseInputStream<?> s3ObjectStream = s3Client.getObject(getObjectRequest)) {
            zos.putNextEntry(new ZipEntry(keyName));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = s3ObjectStream.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
        } catch (IOException | S3Exception e) {
            throw new RuntimeException("Failed to add file to ZIP: " + keyName, e);
        }
    }

    @Override
    public void moveFiles(String sourceBucketName, String destinationBucketName) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(sourceBucketName)
                .build();

        try {
            ListObjectsV2Response listObjectsResponse;
            do {
                listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest);
                List<S3Object> objects = listObjectsResponse.contents();

                for (S3Object object : objects) {
                    String keyName = object.key();
                    // Submit the copy and delete tasks to be executed concurrently
                    executorService.submit(() -> copyAndDeleteObject(sourceBucketName, destinationBucketName, keyName));
                }

            } while (listObjectsResponse.isTruncated());

        } catch (S3Exception e) {
            log.error("Failed to list objects from bucket: {} - {}", sourceBucketName, e.getMessage());
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void copyAndDeleteObject(String sourceBucketName, String destinationBucketName, String keyName) {
        try {
            // Copy file to the destination bucket
            CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                    .sourceBucket(sourceBucketName)
                    .sourceKey(keyName)
                    .destinationBucket(destinationBucketName)
                    .destinationKey(keyName)
                    .build();
            s3Client.copyObject(copyRequest);
            log.info("Copied file: {} from {} to {}", keyName, sourceBucketName, destinationBucketName);

            // Delete file from the source bucket
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(sourceBucketName)
                    .key(keyName)
                    .build();
            s3Client.deleteObject(deleteRequest);
            log.info("Deleted file: {} from {}", keyName, sourceBucketName);

        } catch (S3Exception e) {
            log.error("Error while moving file: {} - {}", keyName, e.getMessage());
        }
    }
}
