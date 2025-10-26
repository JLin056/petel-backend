package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaBase64Entity;
import com.example.petel.entity.PropertyImageEntity;
import com.example.petel.entity.RoomImageEntity;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.MediaBase64Repository;
import com.example.petel.repository.PropertyImageRepository;
import com.example.petel.repository.RoomImageRepository;
import com.example.petel.service.MEDIA002Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

/**
 * MEDIA-002 Base64 圖片更新 Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MEDIA002SvcImpl implements MEDIA002Svc {

    private final MediaBase64Repository mediaBase64Repository;
    private final PropertyImageRepository propertyImageRepository;
    private final RoomImageRepository roomImageRepository;

    /**
     * 更新 Base64 圖片資料 (支援批量)
     * @param req Base64 圖片更新請求
     * @return 更新結果
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MEDIA002Tranrs> updateBase64Media(Req<MEDIA002Tranrq> req) {
        log.info("-------- [MEDIA-002] Base64 圖片更新 ---------");

        MEDIA002Tranrq tranrq = req.getTranrq();
        log.info("[MEDIA-002] 媒體更新數量：{}", tranrq.getMedias().size());

        List<MEDIA002TranrsMediaResult> results = new ArrayList<>();
        int successCount = 0;
        int failedCount = 0;

        // 處理每個媒體更新
        for (int i = 0; i < tranrq.getMedias().size(); i++) {
            MEDIA002TranrqMediaInfo mediaInfo = tranrq.getMedias().get(i);
            log.info("[MEDIA-002] 處理媒體 [{}]：ID={}", i, mediaInfo.getMediaId());

            try {
                // 1. 查詢媒體是否存在
                Optional<MediaBase64Entity> mediaOptional = mediaBase64Repository.findById(mediaInfo.getMediaId());
                if (mediaOptional.isEmpty()) {
                    throw new IllegalArgumentException("媒體ID不存在：" + mediaInfo.getMediaId());
                }

                MediaBase64Entity existingMedia = mediaOptional.get();
                log.info("[MEDIA-002] 找到媒體記錄：{}", existingMedia.getFileName());

                // 2. 更新欄位 (只更新非 null 的欄位)
                boolean isUpdated = false;

                if (mediaInfo.getBase64Data() != null && !mediaInfo.getBase64Data().trim().isEmpty()) {
                    // 驗證 Base64 資料
                    validateBase64Data(mediaInfo.getBase64Data());
                    existingMedia.setBase64Data(mediaInfo.getBase64Data());
                    isUpdated = true;
                    log.info("[MEDIA-002] 更新 Base64 資料");

                    // 如果更新了 Base64 資料且沒有提供 sizeBytes，則重新計算
                    if (mediaInfo.getSizeBytes() == null) {
                        Long calculatedSize = calculateBase64Size(mediaInfo.getBase64Data());
                        existingMedia.setSizeBytes(calculatedSize);
                        log.info("[MEDIA-002] 重新計算檔案大小：{} bytes", calculatedSize);
                    }
                }

                if (mediaInfo.getFileName() != null && !mediaInfo.getFileName().trim().isEmpty()) {
                    existingMedia.setFileName(mediaInfo.getFileName());
                    isUpdated = true;
                    log.info("[MEDIA-002] 更新檔案名稱：{}", mediaInfo.getFileName());
                }

                if (mediaInfo.getMimeType() != null && !mediaInfo.getMimeType().trim().isEmpty()) {
                    existingMedia.setMimeType(mediaInfo.getMimeType());
                    isUpdated = true;
                    log.info("[MEDIA-002] 更新 MIME 類型：{}", mediaInfo.getMimeType());
                }

                if (mediaInfo.getBucket() != null && !mediaInfo.getBucket().trim().isEmpty()) {
                    existingMedia.setBucket(mediaInfo.getBucket());
                    isUpdated = true;
                    log.info("[MEDIA-002] 更新 Bucket：{}", mediaInfo.getBucket());
                }

                if (mediaInfo.getSizeBytes() != null && mediaInfo.getSizeBytes() > 0) {
                    existingMedia.setSizeBytes(mediaInfo.getSizeBytes());
                    isUpdated = true;
                    log.info("[MEDIA-002] 更新檔案大小：{} bytes", mediaInfo.getSizeBytes());
                }

                // 2.1 更新關聯表中的 sortOrder
                if (mediaInfo.getSortOrder() != null) {
                    updateSortOrderInAssociationTable(mediaInfo.getMediaId(), mediaInfo.getSortOrder());
                    isUpdated = true;
                }

                if (!isUpdated) {
                    throw new IllegalArgumentException("未提供任何要更新的欄位");
                }

                // 3. 儲存更新
                MediaBase64Entity updatedMedia = mediaBase64Repository.save(existingMedia);
                log.info("[MEDIA-002] 媒體更新成功，ID={}", updatedMedia.getId());

                // 4. 添加成功結果
                MEDIA002TranrsMediaResult result = new MEDIA002TranrsMediaResult();
                result.setMediaId(updatedMedia.getId());
                result.setBucket(updatedMedia.getBucket());
                result.setFileName(updatedMedia.getFileName());
                result.setSizeBytes(updatedMedia.getSizeBytes());
                result.setMimeType(updatedMedia.getMimeType());
                result.setStatus("SUCCESS");
                result.setErrorMessage(null);

                results.add(result);
                successCount++;

            } catch (Exception e) {
                log.error("[MEDIA-002] 媒體 [{}] 更新失敗：{}", i, e.getMessage(), e);

                // 添加失敗結果
                MEDIA002TranrsMediaResult result = new MEDIA002TranrsMediaResult();
                result.setMediaId(mediaInfo.getMediaId());
                result.setBucket(mediaInfo.getBucket());
                result.setFileName(mediaInfo.getFileName());
                result.setSizeBytes(mediaInfo.getSizeBytes());
                result.setMimeType(mediaInfo.getMimeType());
                result.setStatus("FAILED");
                result.setErrorMessage(e.getMessage());

                results.add(result);
                failedCount++;
            }
        }

        log.info("[MEDIA-002] 批量更新完成，成功：{}，失敗：{}", successCount, failedCount);

        // 5. 建立回應
        MEDIA002Tranrs tranrs = new MEDIA002Tranrs(
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
     * 更新關聯表中的 sortOrder
     * @param mediaId 媒體ID
     * @param newSortOrder 新的排序順序
     */
    private void updateSortOrderInAssociationTable(String mediaId, Integer newSortOrder) {
        log.info("[MEDIA-002] 開始更新 sortOrder，MediaID={}，NewSortOrder={}", mediaId, newSortOrder);

        try {
            // 1. 先查詢 PETEL_PROPERTY_IMAGE
            List<PropertyImageEntity> propertyImages = propertyImageRepository.findByMediaId(mediaId);
            if (!propertyImages.isEmpty()) {
                for (PropertyImageEntity propertyImage : propertyImages) {
                    propertyImage.setSortOrder(newSortOrder);
                    propertyImageRepository.save(propertyImage);
                    log.info("[MEDIA-002] PETEL_PROPERTY_IMAGE sortOrder 更新成功，PropertyID={}, MediaID={}, SortOrder={}",
                            propertyImage.getPropertyId(), mediaId, newSortOrder);
                }
                return;
            }

            // 2. 如果沒找到，查詢 PETEL_ROOM_IMAGE
            List<RoomImageEntity> roomImages = roomImageRepository.findByMediaId(mediaId);
            if (!roomImages.isEmpty()) {
                for (RoomImageEntity roomImage : roomImages) {
                    roomImage.setSortOrder(newSortOrder);
                    roomImageRepository.save(roomImage);
                    log.info("[MEDIA-002] PETEL_ROOM_IMAGE sortOrder 更新成功，RoomID={}, MediaID={}, SortOrder={}",
                            roomImage.getRoomId(), mediaId, newSortOrder);
                }
                return;
            }

            // 3. 如果都沒找到，記錄警告
            log.warn("[MEDIA-002] 未找到 MediaID={} 的關聯記錄，無法更新 sortOrder", mediaId);

        } catch (Exception e) {
            log.error("[MEDIA-002] 更新 sortOrder 失敗：{}", e.getMessage(), e);
            // 不拋出異常，允許其他欄位的更新繼續進行
        }
    }

    /**
     * 驗證 Base64 資料是否有效
     * @param base64Data Base64 字串
     * @throws IllegalArgumentException 如果資料無效
     */
    private void validateBase64Data(String base64Data) throws IllegalArgumentException {
        if (base64Data == null || base64Data.trim().isEmpty()) {
            throw new IllegalArgumentException("Base64 資料不得為空");
        }

        try {
            // 移除可能的 data URL 前綴 (例如：data:image/png;base64,)
            String cleanedBase64 = base64Data;
            if (base64Data.contains(",")) {
                cleanedBase64 = base64Data.split(",")[1];
            }

            // 嘗試解碼驗證
            Base64.getDecoder().decode(cleanedBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Base64 資料格式無效");
        }
    }

    /**
     * 計算 Base64 字串解碼後的檔案大小
     * @param base64Data Base64 字串
     * @return 檔案大小（bytes）
     */
    private Long calculateBase64Size(String base64Data) {
        try {
            // 移除可能的 data URL 前綴
            String cleanedBase64 = base64Data;
            if (base64Data.contains(",")) {
                cleanedBase64 = base64Data.split(",")[1];
            }

            // 解碼並計算大小
            byte[] decodedBytes = Base64.getDecoder().decode(cleanedBase64);
            return (long) decodedBytes.length;
        } catch (Exception e) {
            log.warn("[MEDIA-002] 無法計算 Base64 大小，使用預設值 0");
            return 0L;
        }
    }
}
