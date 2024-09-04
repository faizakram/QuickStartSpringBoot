package com.app.controller;

import com.app.service.S3Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("file") MultipartFile file,
                                             @RequestParam(value = "isReadPublicly", defaultValue = "false") boolean isReadPublicly) {
        boolean isUploaded = s3Service.uploadFile(file, isReadPublicly);
        if (isUploaded) {
            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } else {
            return ResponseEntity.status(500).body("Failed to upload file: " + file.getOriginalFilename());
        }
    }

    @GetMapping("/download/{key}")
    public StreamingResponseBody downloadFile(@PathVariable String key, HttpServletResponse httpResponse) {

        httpResponse.setContentType("application/octet-stream");
        httpResponse.setHeader("Content-Disposition", String.format("inline; filename=\"%s\"", key));

        CompletableFuture<ByteArrayInputStream> byteArrayInputStreamCompletableFuture = s3Service.downloadFileAsStream(key);

        return outputStream -> {
            ByteArrayInputStream byteArrayInputStream = byteArrayInputStreamCompletableFuture.join();
            if (byteArrayInputStream != null) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = byteArrayInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } else {
                // Handle the case where the stream is null
                httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                String errorMessage = "Failed to download the key. Please try again later.";
                outputStream.write(errorMessage.getBytes());
                outputStream.flush();
            }
        };
    }
}
