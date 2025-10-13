package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaEntity;
import com.example.petel.entity.MediaS3Entity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.MediaRepository;
import com.example.petel.repository.MediaS3Repository;
import com.example.petel.service.IMG001Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

/**
 * IMG-001 圖片上傳確認 Service Implementation
 * (使用 Presigned URL 上傳後的確認)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IMG001SvcImpl implements IMG001Svc {

    private final MediaRepository mediaRepository;
    private final MediaS3Repository mediaS3Repository;
    private final S3Client s3Client;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    /**
     * 確認圖片已上傳到 S3 並儲存到資料庫
     * @param req 圖片上傳確認請求
     * @return 上傳結果
     * @throws InsertFailException 確認失敗異常
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<IMG001Tranrs> uploadImage(Req<IMG001Tranrq> req) throws InsertFailException {
        log.info("-------- [IMG-001] 圖片上傳確認 ---------");

        IMG001Tranrq tranrq = req.getTranrq();

        try {
            // 1. 驗證檔案是否已成功上傳到 S3
            verifyS3Object(tranrq.getBucket(), tranrq.getObjectKey());
            log.info("[IMG-001] S3 檔案驗證成功，Object Key：{}", tranrq.getObjectKey());

            // 2. 儲存 PETEL_MEDIA 記錄
            MediaEntity mediaEntity = new MediaEntity();
            mediaEntity.setStorageType("S3");
            mediaEntity.setMimeType(tranrq.getMimeType());
            mediaEntity.setSizeBytes(tranrq.getSizeBytes());
            mediaEntity.setVisibility(tranrq.getVisibility() != null ? tranrq.getVisibility() : "PUBLIC");

            MediaEntity savedMedia = mediaRepository.save(mediaEntity);
            log.info("[IMG-001] PETEL_MEDIA 記錄建立成功，ID={}", savedMedia.getId());

            // 3. 儲存 PETEL_MEDIA_S3 記錄
            MediaS3Entity mediaS3Entity = new MediaS3Entity();
            mediaS3Entity.setMediaId(savedMedia.getId());
            mediaS3Entity.setBucket(tranrq.getBucket());
            mediaS3Entity.setObjectKey(tranrq.getObjectKey());

            mediaS3Repository.save(mediaS3Entity);
            log.info("[IMG-001] PETEL_MEDIA_S3 記錄建立成功");

            // 4. 構造公開 URL
            String objectUrl = buildObjectUrl(tranrq.getBucket(), tranrq.getObjectKey());

            // 5. 建立回應
            IMG001Tranrs tranrs = new IMG001Tranrs(
                savedMedia.getId(),
                tranrq.getBucket(),
                tranrq.getObjectKey(),
                objectUrl,
                savedMedia.getSizeBytes(),
                savedMedia.getMimeType(),
                savedMedia.getVisibility()
            );

            return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
            );

        } catch (Exception e) {
            log.error("[IMG-001] 圖片上傳確認失敗：{}", e.getMessage(), e);
            throw new InsertFailException("圖片上傳確認失敗：" + e.getMessage());
        }
    }

    /**
     * 驗證 S3 物件是否存在
     * @param bucket S3 Bucket
     * @param objectKey S3 Object Key
     * @throws Exception 如果物件不存在
     */
    private void verifyS3Object(String bucket, String objectKey) throws Exception {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            log.info("[IMG-001] S3 物件存在，大小：{} bytes", headObjectResponse.contentLength());
        } catch (Exception e) {
            log.error("[IMG-001] S3 物件不存在或無法存取：{}", objectKey);
            throw new Exception("S3 檔案不存在或無法存取");
        }
    }

    /**
     * 建立公開存取 URL
     * @param bucket S3 Bucket
     * @param objectKey S3 Object Key
     * @return 完整 URL
     */
    private String buildObjectUrl(String bucket, String objectKey) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
            bucket,
            awsRegion,
            objectKey);
    }
}
