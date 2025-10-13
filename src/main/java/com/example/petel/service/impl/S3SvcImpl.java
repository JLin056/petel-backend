package com.example.petel.service.impl;

import com.example.petel.dto.S3SignUploadReq;
import com.example.petel.dto.S3SignatureRes;
import com.example.petel.service.S3Svc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3SvcImpl implements S3Svc {

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    // 使用 S3Presigner 處理簽名邏輯
    private final S3Presigner s3Presigner;

    @Override
    public S3SignatureRes generatePresignedUrl(S3SignUploadReq s3SignUploadReq) {
        // 1. 構造 Object Key (S3 檔案路徑)
        String objectKey = "lodgings/" + s3SignUploadReq.getLodgingId() + "/" + System.currentTimeMillis() + "-" + s3SignUploadReq.getFilename();

        // 2. 構造 PutObject 請求，設定 ACL 為 public-read
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType(s3SignUploadReq.getFileType())
                .acl("public-read") // 確保上傳後檔案是公開可讀取
                .build();

        // 3. 創建預簽名請求，設定有效期限（例如 2 分鐘）
        software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest presignedRequest =
                s3Presigner.presignPutObject(r -> r.signatureDuration(Duration.ofMinutes(2)).putObjectRequest(objectRequest));

        // 4. 構造最終的公開 Object URL
        String publicUrl = String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                awsRegion, // 使用注入的屬性
                objectKey);

        S3SignatureRes s3SignatureRes = new S3SignatureRes();
        s3SignatureRes.setSignedUrl(presignedRequest.url().toString());// 簽名 URL
        s3SignatureRes.setObjectKey(objectKey); // 檔案 Key
        s3SignatureRes.setObjectUrl(publicUrl); // 公開 URL
        return s3SignatureRes;
    }
}
