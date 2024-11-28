package com.wap.wapor.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class GCSFileUploadService {

    private final Storage storage;

    @Value("${gcp.storage.bucket-name}")
    private String bucketName;

    @Value("${gcp.storage.base-url}")
    private String baseUrl;

    public GCSFileUploadService(Storage storage) {
        this.storage = storage;
    }

    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            // BlobInfo 생성
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
                    .setContentType(file.getContentType())
                    .build();

            // 파일 업로드
            storage.create(blobInfo, file.getBytes());

            // 업로드된 파일의 URL 반환
            return baseUrl + "/" + bucketName + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
        }
    }
}
