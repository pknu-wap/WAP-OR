package com.wap.wapor.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GCPConfig {

    @Bean
    public Storage storage() throws IOException {
        // resources 폴더의 JSON 키 파일을 로드
        InputStream credentialsStream = getClass().getClassLoader()
                .getResourceAsStream("sapient-tangent-442814-u3-87279629ee20.json");

        if (credentialsStream == null) {
            throw new RuntimeException("Service account key file not found in resources folder");
        }

        return StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(credentialsStream))
                .build()
                .getService();
    }
}
