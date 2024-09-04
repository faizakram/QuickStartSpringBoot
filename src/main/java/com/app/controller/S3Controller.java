package com.app.controller;

import com.app.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

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
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String key) {
        InputStream fileStream = s3Service.downloadFileAsStream(key);
        InputStreamResource resource = new InputStreamResource(fileStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + key)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
