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

import java.util.ArrayList;
import java.util.List;

/**
 * IMG-001 圖片上傳確認 Service Implementation
 * (使用 Presigned URL 上傳後的確認，支援批量上傳)
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
     * 確認圖片已上傳到 S3 並儲存到資料庫 (支援批量)
     * @param req 圖片上傳確認請求
     * @return 上傳結果
     * @throws InsertFailException 確認失敗異常
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<IMG001Tranrs> uploadImage(Req<IMG001Tranrq> req) throws InsertFailException {
        log.info("-------- [IMG-001] 圖片上傳確認 ---------");

        IMG001Tranrq tranrq = req.getTranrq();
        log.info("[IMG-001] 分類：{}, 關聯ID：{}, 媒體數量：{}",
                 tranrq.getCategory(), tranrq.getReferenceId(), tranrq.getMedias().size());

        List<IMG001TranrsMediaResult> results = new ArrayList<>();
        int successCount = 0;
        int failedCount = 0;

        // 處理每個媒體上傳
        for (int i = 0; i < tranrq.getMedias().size(); i++) {
            IMG001TranrqMediaInfo mediaInfo = tranrq.getMedias().get(i);
            log.info("[IMG-001] 處理媒體 [{}]：{}", i, mediaInfo.getObjectKey());

            try {
                // 1. 驗證檔案是否已成功上傳到 S3
                verifyS3Object(mediaInfo.getBucket(), mediaInfo.getObjectKey());
                log.info("[IMG-001] S3 檔案驗證成功，Object Key：{}", mediaInfo.getObjectKey());

                // 2. 生成媒體ID (M000000001 格式)
                String mediaId = generateMediaId();
                log.info("[IMG-001] 生成媒體ID：{}", mediaId);

                // 3. 儲存 PETEL_MEDIA 記錄
                MediaEntity mediaEntity = new MediaEntity();
                mediaEntity.setId(mediaId);
                mediaEntity.setStorageType("S3");
                mediaEntity.setMimeType(mediaInfo.getMimeType());
                mediaEntity.setSizeBytes(mediaInfo.getSizeBytes());
                mediaEntity.setVisibility(mediaInfo.getVisibility() != null ? mediaInfo.getVisibility() : "PUBLIC");

                MediaEntity savedMedia = mediaRepository.save(mediaEntity);
                log.info("[IMG-001] PETEL_MEDIA 記錄建立成功，ID={}", savedMedia.getId());

                // 4. 儲存 PETEL_MEDIA_S3 記錄
                MediaS3Entity mediaS3Entity = new MediaS3Entity();
                mediaS3Entity.setMediaId(savedMedia.getId());
                mediaS3Entity.setBucket(mediaInfo.getBucket());
                mediaS3Entity.setObjectKey(mediaInfo.getObjectKey());

                mediaS3Repository.save(mediaS3Entity);
                log.info("[IMG-001] PETEL_MEDIA_S3 記錄建立成功");

                // 5. 構造公開 URL
                String objectUrl = buildObjectUrl(mediaInfo.getBucket(), mediaInfo.getObjectKey());

                // 6. 添加成功結果
                IMG001TranrsMediaResult result = new IMG001TranrsMediaResult();
                result.setMediaId(savedMedia.getId());
                result.setBucket(mediaInfo.getBucket());
                result.setObjectKey(mediaInfo.getObjectKey());
                result.setObjectUrl(objectUrl);
                result.setSizeBytes(savedMedia.getSizeBytes());
                result.setMimeType(savedMedia.getMimeType());
                result.setVisibility(savedMedia.getVisibility());
                result.setStatus("SUCCESS");
                result.setErrorMessage(null);

                results.add(result);
                successCount++;

            } catch (Exception e) {
                log.error("[IMG-001] 媒體 [{}] 上傳確認失敗：{}", i, e.getMessage(), e);

                // 添加失敗結果
                IMG001TranrsMediaResult result = new IMG001TranrsMediaResult();
                result.setMediaId(null);
                result.setBucket(mediaInfo.getBucket());
                result.setObjectKey(mediaInfo.getObjectKey());
                result.setObjectUrl(null);
                result.setSizeBytes(mediaInfo.getSizeBytes());
                result.setMimeType(mediaInfo.getMimeType());
                result.setVisibility(mediaInfo.getVisibility());
                result.setStatus("FAILED");
                result.setErrorMessage(e.getMessage());

                results.add(result);
                failedCount++;
            }
        }

        log.info("[IMG-001] 批量上傳完成，成功：{}，失敗：{}", successCount, failedCount);

        // 7. 建立回應
        IMG001Tranrs tranrs = new IMG001Tranrs(
            tranrq.getCategory(),
            tranrq.getReferenceId(),
            successCount,
            failedCount,
            results
        );

        return new Res<>(
            new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
            tranrs
        );
    }

    /**
     * 生成媒體ID (M000000001 格式)
     * 格式：M + 9位數字
     * @return 媒體ID
     */
    private synchronized String generateMediaId() {
        // 1. 查詢資料庫中最大的 ID
        List<MediaEntity> allMedia = mediaRepository.findAll();

        long maxNumber = 0;
        for (MediaEntity media : allMedia) {
            String id = media.getId();
            if (id != null && id.startsWith("M") && id.length() == 10) {
                try {
                    long number = Long.parseLong(id.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // 忽略無效的 ID 格式
                }
            }
        }

        // 2. 生成新的 ID (最大值 + 1)
        long newNumber = maxNumber + 1;

        // 3. 格式化為 M000000001
        return String.format("M%09d", newNumber);
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
