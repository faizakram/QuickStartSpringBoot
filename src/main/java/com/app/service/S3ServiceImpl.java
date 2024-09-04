package com.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedUpload;
import software.amazon.awssdk.transfer.s3.model.Upload;
import software.amazon.awssdk.transfer.s3.model.UploadRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3AsyncClient s3AsyncClient;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    private S3TransferManager createTransferManager() {
        return S3TransferManager.builder()
                .s3Client(s3AsyncClient)
                .build();
    }


    @Override
    public boolean uploadFile(MultipartFile file, boolean isReadPublicly) {
        log.info("Started uploading file '{}' to S3 Bucket '{}'", file.getOriginalFilename(), bucketName);
        try (S3TransferManager transferManager = createTransferManager()) {
            UploadRequest uploadRequest;
            if (isReadPublicly) {
                uploadRequest = UploadRequest.builder()
                        .putObjectRequest(builder -> builder.bucket(bucketName).key(file.getOriginalFilename()).acl("public-read"))
                        .requestBody(AsyncRequestBody.fromBytes(file.getBytes()))
                        .addTransferListener(LoggingTransferListener.create()) // For logging progress
                        .build();
            } else {
                uploadRequest = UploadRequest.builder()
                        .putObjectRequest(builder -> builder.bucket(bucketName).key(file.getOriginalFilename()))
                        .requestBody(AsyncRequestBody.fromBytes(file.getBytes()))
                        .addTransferListener(LoggingTransferListener.create()) // For logging progress
                        .build();
            }
            // Start the file upload
            Upload upload = transferManager.upload(uploadRequest);

            // Wait for the upload to complete
            CompletableFuture<CompletedUpload> uploadCompletion = upload.completionFuture();
            uploadCompletion.join();
            log.info("Successfully uploaded file to S3. Bucket: {}, Key: {}", bucketName, file.getOriginalFilename());
            return true;
        } catch (Exception e) {
            log.error("Failed to upload file to S3. Bucket: {}, Key: {}", bucketName, file.getOriginalFilename(), e);
            return false;
        }
    }

    @Override
    public CompletableFuture<ByteArrayInputStream> downloadFileAsStream(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Download the file directly into a ByteArrayOutputStream
        return s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes())
                .thenApply(response -> {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        byteArrayOutputStream.write(response.asByteArray());
                    } catch (Exception e) {
                        log.error("Failed to write response to ByteArrayOutputStream. Bucket: {}, Key: {}", bucketName, key, e);
                    }
                    return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                })
                .exceptionally(e -> {
                    log.error("Failed to download file from S3. Bucket: {}, Key: {}", bucketName, key, e);
                    return null;
                });
    }
}
