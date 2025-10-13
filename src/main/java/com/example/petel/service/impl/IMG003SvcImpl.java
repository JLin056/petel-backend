package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaEntity;
import com.example.petel.entity.MediaS3Entity;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.MediaRepository;
import com.example.petel.repository.MediaS3Repository;
import com.example.petel.service.IMG003Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.Optional;

/**
 * IMG-003 圖片刪除 Service Implementation
 * (刪除 S3 檔案並移除資料庫記錄)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IMG003SvcImpl implements IMG003Svc {

    private final MediaRepository mediaRepository;
    private final MediaS3Repository mediaS3Repository;
    private final S3Client s3Client;

    /**
     * 刪除圖片 (刪除 S3 檔案並移除資料庫記錄)
     * @param req 圖片刪除請求
     * @return 刪除結果
     * @throws DeleteFailException 刪除失敗異常
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<IMG003Tranrs> deleteImage(Req<IMG003Tranrq> req) throws DeleteFailException {
        log.info("-------- [IMG-003] 圖片刪除 ---------");

        IMG003Tranrq tranrq = req.getTranrq();

        try {
            // 1. 檢查媒體記錄是否存在
            Optional<MediaEntity> mediaOptional = mediaRepository.findById(tranrq.getMediaId());
            if (mediaOptional.isEmpty()) {
                log.error("[IMG-003] 媒體記錄不存在，ID={}", tranrq.getMediaId());
                throw new DeleteFailException("媒體記錄不存在");
            }
            MediaEntity mediaEntity = mediaOptional.get();
            log.info("[IMG-003] 找到媒體記錄，ID={}", mediaEntity.getId());

            // 2. 檢查 S3 記錄是否存在
            Optional<MediaS3Entity> mediaS3Optional = mediaS3Repository.findById(tranrq.getMediaId());
            if (mediaS3Optional.isEmpty()) {
                log.error("[IMG-003] S3 記錄不存在，Media ID={}", tranrq.getMediaId());
                throw new DeleteFailException("S3 記錄不存在");
            }
            MediaS3Entity mediaS3Entity = mediaS3Optional.get();
            String bucket = mediaS3Entity.getBucket();
            String objectKey = mediaS3Entity.getObjectKey();
            log.info("[IMG-003] 找到 S3 記錄，Object Key={}", objectKey);

            // 3. 從 S3 刪除檔案
            deleteS3Object(bucket, objectKey);
            log.info("[IMG-003] S3 檔案已刪除，Object Key：{}", objectKey);

            // 4. 從資料庫刪除 S3 記錄 (先刪除子表)
            mediaS3Repository.delete(mediaS3Entity);
            log.info("[IMG-003] PETEL_MEDIA_S3 記錄已刪除");

            // 5. 從資料庫刪除媒體記錄 (再刪除主表)
            mediaRepository.delete(mediaEntity);
            log.info("[IMG-003] PETEL_MEDIA 記錄已刪除，ID={}", tranrq.getMediaId());

            // 6. 建立回應
            IMG003Tranrs tranrs = new IMG003Tranrs(
                tranrq.getMediaId(),
                "圖片刪除成功"
            );

            return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
            );

        } catch (Exception e) {
            log.error("[IMG-003] 圖片刪除失敗：{}", e.getMessage(), e);
            throw new DeleteFailException("圖片刪除失敗：" + e.getMessage());
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
            log.info("[IMG-003] S3 物件已刪除：{}", objectKey);
        } catch (Exception e) {
            log.error("[IMG-003] S3 物件刪除失敗：{}，錯誤：{}", objectKey, e.getMessage());
            // 不拋出異常，允許繼續執行 (檔案可能已不存在)
        }
    }
}
