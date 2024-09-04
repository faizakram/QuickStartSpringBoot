package com.app.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;

public interface S3Service {


    boolean uploadFile(MultipartFile file, boolean isReadPublicly);

    CompletableFuture<ByteArrayInputStream> downloadFileAsStream(String key);
}
