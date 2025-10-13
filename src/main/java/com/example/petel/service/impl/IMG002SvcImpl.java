package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaEntity;
import com.example.petel.entity.MediaS3Entity;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.MediaRepository;
import com.example.petel.repository.MediaS3Repository;
import com.example.petel.service.IMG002Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.util.Optional;

/**
 * IMG-002 圖片更新 Service Implementation
 * (替換 S3 檔案並更新資料庫)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IMG002SvcImpl implements IMG002Svc {

    private final MediaRepository mediaRepository;
    private final MediaS3Repository mediaS3Repository;
    private final S3Client s3Client;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    /**
     * 更新圖片 (替換 S3 檔案並更新資料庫)
     * @param req 圖片更新請求
     * @return 更新結果
     * @throws UpdateFailException 更新失敗異常
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<IMG002Tranrs> updateImage(Req<IMG002Tranrq> req) throws UpdateFailException {
        log.info("-------- [IMG-002] 圖片更新 (替換) ---------");

        IMG002Tranrq tranrq = req.getTranrq();

        try {
            // 1. 檢查媒體記錄是否存在
            Optional<MediaEntity> mediaOptional = mediaRepository.findById(tranrq.getMediaId());
            if (mediaOptional.isEmpty()) {
                log.error("[IMG-002] 媒體記錄不存在，ID={}", tranrq.getMediaId());
                throw new UpdateFailException("媒體記錄不存在");
            }
            MediaEntity mediaEntity = mediaOptional.get();
            log.info("[IMG-002] 找到媒體記錄，ID={}", mediaEntity.getId());

            // 2. 檢查 S3 記錄是否存在
            Optional<MediaS3Entity> mediaS3Optional = mediaS3Repository.findById(tranrq.getMediaId());
            if (mediaS3Optional.isEmpty()) {
                log.error("[IMG-002] S3 記錄不存在，Media ID={}", tranrq.getMediaId());
                throw new UpdateFailException("S3 記錄不存在");
            }
            MediaS3Entity mediaS3Entity = mediaS3Optional.get();
            String oldBucket = mediaS3Entity.getBucket();
            String oldObjectKey = mediaS3Entity.getObjectKey();
            log.info("[IMG-002] 找到 S3 記錄，舊 Object Key={}", oldObjectKey);

            // 3. 驗證新檔案是否已成功上傳到 S3
            verifyS3Object(tranrq.getNewBucket(), tranrq.getNewObjectKey());
            log.info("[IMG-002] S3 新檔案驗證成功，Object Key：{}", tranrq.getNewObjectKey());

            // 4. 刪除舊的 S3 檔案
            deleteS3Object(oldBucket, oldObjectKey);
            log.info("[IMG-002] 舊 S3 檔案已刪除，Object Key：{}", oldObjectKey);

            // 5. 更新 PETEL_MEDIA 記錄
            mediaEntity.setMimeType(tranrq.getMimeType());
            mediaEntity.setSizeBytes(tranrq.getSizeBytes());
            if (tranrq.getVisibility() != null) {
                mediaEntity.setVisibility(tranrq.getVisibility());
            }
            MediaEntity updatedMedia = mediaRepository.save(mediaEntity);
            log.info("[IMG-002] PETEL_MEDIA 記錄已更新，ID={}", updatedMedia.getId());

            // 6. 更新 PETEL_MEDIA_S3 記錄
            mediaS3Entity.setBucket(tranrq.getNewBucket());
            mediaS3Entity.setObjectKey(tranrq.getNewObjectKey());
            mediaS3Repository.save(mediaS3Entity);
            log.info("[IMG-002] PETEL_MEDIA_S3 記錄已更新");

            // 7. 構造公開 URL
            String objectUrl = buildObjectUrl(tranrq.getNewBucket(), tranrq.getNewObjectKey());

            // 8. 建立回應
            IMG002Tranrs tranrs = new IMG002Tranrs(
                updatedMedia.getId(),
                tranrq.getNewBucket(),
                tranrq.getNewObjectKey(),
                objectUrl,
                updatedMedia.getSizeBytes(),
                updatedMedia.getMimeType(),
                updatedMedia.getVisibility(),
                updatedMedia.getStorageType()
            );

            return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
            );

        } catch (Exception e) {
            log.error("[IMG-002] 圖片更新失敗：{}", e.getMessage(), e);
            throw new UpdateFailException("圖片更新失敗：" + e.getMessage());
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
            log.info("[IMG-002] S3 物件存在，大小：{} bytes", headObjectResponse.contentLength());
        } catch (Exception e) {
            log.error("[IMG-002] S3 物件不存在或無法存取：{}", objectKey);
            throw new Exception("S3 檔案不存在或無法存取");
        }
    }

    /**
     * 刪除 S3 物件
     * @param bucket S3 Bucket
     * @param objectKey S3 Object Key
     */
    private void deleteS3Object(String bucket, String objectKey) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("[IMG-002] S3 物件已刪除：{}", objectKey);
        } catch (Exception e) {
            log.error("[IMG-002] S3 物件刪除失敗：{}，錯誤：{}", objectKey, e.getMessage());
            // 不拋出異常，允許繼續執行 (舊檔案可能已不存在)
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
