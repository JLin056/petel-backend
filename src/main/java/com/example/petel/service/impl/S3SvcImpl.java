package com.example.petel.service.impl;

import com.example.petel.dto.S3SignUploadReq;
import com.example.petel.dto.S3SignUploadReqFileInfo;
import com.example.petel.dto.S3SignatureRes;
import com.example.petel.dto.S3SignatureResUrlInfo;
import com.example.petel.service.S3Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * S3 Service Implementation
 * 支援單檔與批量上傳 Presigned URL 生成
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3SvcImpl implements S3Svc {

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    private final S3Presigner s3Presigner;

    /**
     * 生成 Presigned URL (支援單檔與批量)
     * @param req 上傳請求
     * @return Presigned URL 資訊
     */
    @Override
    public S3SignatureRes generatePresignedUrl(S3SignUploadReq req) {
        log.info("-------- [S3] 生成 Presigned URL ---------");
        log.info("[S3] 檔案分類：{}, 關聯ID：{}, 檔案數量：{}",
                req.getCategory(), req.getReferenceId(), req.getFiles().size());

        List<S3SignatureResUrlInfo> uploads = new ArrayList<>();

        for (int i = 0; i < req.getFiles().size(); i++) {
            S3SignUploadReqFileInfo fileInfo = req.getFiles().get(i);

            // 1. 構造 Object Key (含分類前綴)
            String objectKey = buildObjectKey(
                    req.getCategory(),
                    req.getReferenceId(),
                    fileInfo.getFilename()
            );
            log.info("[S3] 生成 Object Key [{}]：{}", i, objectKey);

            // 2. 構造 PutObject 請求
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentType(fileInfo.getFileType())
                    .build();

            // 3. 創建預簽名請求 (15 分鐘有效)
            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(
                    r -> r.signatureDuration(Duration.ofMinutes(15))
                            .putObjectRequest(objectRequest)
            );

            // 4. 構造公開 URL
            String publicUrl = buildPublicUrl(objectKey);

            // 5. 添加到列表
            uploads.add(new S3SignatureResUrlInfo(
                    i,
                    fileInfo.getFilename(),
                    presignedRequest.url().toString(),
                    objectKey,
                    publicUrl
            ));
        }

        log.info("[S3] 成功生成 {} 個 Presigned URL", uploads.size());

        return new S3SignatureRes(
                req.getCategory(),
                req.getReferenceId(),
                uploads
        );
    }

    /**
     * 構造 S3 Object Key (含分類前綴)
     * @param category 分類 (User_Profile, Property_Facility, Room_Image)
     * @param referenceId 關聯ID (可選)
     * @param filename 檔案名稱
     * @return Object Key
     */
    private String buildObjectKey(String category, String referenceId, String filename) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String safeFilename = filename.replaceAll("[^a-zA-Z0-9._-]", "_");

        if (referenceId != null && !referenceId.isEmpty()) {
            return String.format("%s/%s/%s-%s", category, referenceId, timestamp, safeFilename);
        } else {
            return String.format("%s/%s-%s", category, timestamp, safeFilename);
        }
    }

    /**
     * 構造公開存取 URL
     * @param objectKey S3 Object Key
     * @return 完整 URL
     */
    private String buildPublicUrl(String objectKey) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                awsRegion,
                objectKey);
    }
}
