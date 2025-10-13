package com.example.petel.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsConfig {

    // 注入 Access Key ID 和 Secret Key
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKeyId;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretAccessKey;

    // 注入地區資訊
    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Bean
    public S3Presigner s3Presigner() {

        // 1. 建立靜態憑證提供者 (StaticCredentialsProvider)
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        // 2. 使用憑證和 Region 建立 S3Presigner
        return S3Presigner.builder()
                .region(Region.of(awsRegion)) // 確保 Region 已設定
                .credentialsProvider(credentialsProvider) // 顯式傳入密鑰
                .build();
    }
}
