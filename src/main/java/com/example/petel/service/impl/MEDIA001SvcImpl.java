package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaBase64Entity;
import com.example.petel.entity.PropertyImageEntity;
import com.example.petel.entity.RoomImageEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.MediaBase64Repository;
import com.example.petel.repository.PropertyImageRepository;
import com.example.petel.repository.RoomImageRepository;
import com.example.petel.service.MEDIA001Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * MEDIA-001 Base64 圖片上傳 Service Implementation
 * (直接儲存 Base64 到資料庫，不使用 S3)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MEDIA001SvcImpl implements MEDIA001Svc {

    private final MediaBase64Repository mediaBase64Repository;
    private final PropertyImageRepository propertyImageRepository;
    private final RoomImageRepository roomImageRepository;

    /**
     * 上傳 Base64 圖片並儲存到資料庫 (支援批量)
     * @param req Base64 圖片上傳請求
     * @return 上傳結果
     * @throws InsertFailException 上傳失敗異常
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MEDIA001Tranrs> uploadBase64Media(Req<MEDIA001Tranrq> req) throws InsertFailException {
        log.info("-------- [MEDIA-001] Base64 圖片上傳 ---------");

        MEDIA001Tranrq tranrq = req.getTranrq();
        log.info("[MEDIA-001] 分類：{}, 關聯ID：{}, 媒體數量：{}",
                 tranrq.getCategory(), tranrq.getReferenceId(), tranrq.getMedias().size());

        List<MEDIA001TranrsMediaResult> results = new ArrayList<>();
        int successCount = 0;
        int failedCount = 0;

        // 處理每個媒體上傳
        for (int i = 0; i < tranrq.getMedias().size(); i++) {
            MEDIA001TranrqMediaInfo mediaInfo = tranrq.getMedias().get(i);
            log.info("[MEDIA-001] 處理媒體 [{}]：{}", i, mediaInfo.getFileName());

            try {
                // 1. 驗證 Base64 資料
                validateBase64Data(mediaInfo.getBase64Data());
                log.info("[MEDIA-001] Base64 資料驗證成功");

                // 2. 計算檔案大小（如果未提供）
                Long sizeBytes = mediaInfo.getSizeBytes();
                if (sizeBytes == null || sizeBytes == 0) {
                    sizeBytes = calculateBase64Size(mediaInfo.getBase64Data());
                }
                log.info("[MEDIA-001] 檔案大小：{} bytes", sizeBytes);

                // 3. 生成媒體ID (M000000001 格式)
                String mediaId = generateMediaId();
                log.info("[MEDIA-001] 生成媒體ID：{}", mediaId);

                // 4. 儲存 PETEL_MEDIA_BASE64 記錄
                MediaBase64Entity mediaBase64Entity = new MediaBase64Entity();
                mediaBase64Entity.setId(mediaId);
                mediaBase64Entity.setBase64Data(mediaInfo.getBase64Data());
                mediaBase64Entity.setBucket(mediaInfo.getBucket());
                mediaBase64Entity.setFileName(mediaInfo.getFileName());
                mediaBase64Entity.setMimeType(mediaInfo.getMimeType());
                mediaBase64Entity.setSizeBytes(sizeBytes);

                MediaBase64Entity savedMedia = mediaBase64Repository.save(mediaBase64Entity);
                log.info("[MEDIA-001] PETEL_MEDIA_BASE64 記錄建立成功，ID={}", savedMedia.getId());

                // 5. 根據 category 儲存到關聯表
                saveToAssociationTable(tranrq.getCategory(), tranrq.getReferenceId(), savedMedia.getId(), mediaInfo.getSortOrder());

                // 6. 添加成功結果
                MEDIA001TranrsMediaResult result = new MEDIA001TranrsMediaResult();
                result.setMediaId(savedMedia.getId());
                result.setBucket(savedMedia.getBucket());
                result.setFileName(savedMedia.getFileName());
                result.setSizeBytes(savedMedia.getSizeBytes());
                result.setMimeType(savedMedia.getMimeType());
                result.setVisibility(mediaInfo.getVisibility() != null ? mediaInfo.getVisibility() : "PRIVATE");
                result.setStatus("SUCCESS");
                result.setErrorMessage(null);

                results.add(result);
                successCount++;

            } catch (Exception e) {
                log.error("[MEDIA-001] 媒體 [{}] 上傳失敗：{}", i, e.getMessage(), e);

                // 添加失敗結果
                MEDIA001TranrsMediaResult result = new MEDIA001TranrsMediaResult();
                result.setMediaId(null);
                result.setBucket(mediaInfo.getBucket());
                result.setFileName(mediaInfo.getFileName());
                result.setSizeBytes(mediaInfo.getSizeBytes());
                result.setMimeType(mediaInfo.getMimeType());
                result.setVisibility(mediaInfo.getVisibility());
                result.setStatus("FAILED");
                result.setErrorMessage(e.getMessage());

                results.add(result);
                failedCount++;
            }
        }

        log.info("[MEDIA-001] 批量上傳完成，成功：{}，失敗：{}", successCount, failedCount);

        // 7. 建立回應
        MEDIA001Tranrs tranrs = new MEDIA001Tranrs(
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
     * 儲存到關聯表 (PETEL_PROPERTY_IMAGE 或 PETEL_ROOM_IMAGE)
     * @param category 檔案分類
     * @param referenceId 關聯ID (propertyId 或 roomId)
     * @param mediaId 媒體ID
     * @param frontendSortOrder 前端指定的排序順序（選填）
     */
    private void saveToAssociationTable(String category, String referenceId, String mediaId, Integer frontendSortOrder) {
        // 如果沒有 referenceId，則不儲存關聯
        if (referenceId == null || referenceId.trim().isEmpty()) {
            log.info("[MEDIA-001] 無 referenceId，跳過關聯表儲存");
            return;
        }

        try {
            if ("Property_Image".equalsIgnoreCase(category)) {
                // 儲存到 PETEL_PROPERTY_IMAGE
                Integer sortOrder;
                if (frontendSortOrder != null && frontendSortOrder > 0) {
                    // 優先使用前端指定的 sortOrder
                    sortOrder = frontendSortOrder;
                    log.info("[MEDIA-001] 使用前端指定的 sortOrder: {}", sortOrder);
                } else {
                    // 自動計算 sortOrder
                    Integer maxSortOrder = propertyImageRepository.findMaxSortOrderByPropertyId(referenceId);
                    sortOrder = (maxSortOrder != null ? maxSortOrder : 0) + 1;
                    log.info("[MEDIA-001] 自動計算 sortOrder: {}", sortOrder);
                }

                PropertyImageEntity propertyImage = new PropertyImageEntity();
                propertyImage.setPropertyId(referenceId);
                propertyImage.setMediaId(mediaId);
                propertyImage.setSortOrder(sortOrder);

                propertyImageRepository.save(propertyImage);
                log.info("[MEDIA-001] PETEL_PROPERTY_IMAGE 記錄建立成功，PropertyID={}, MediaID={}, SortOrder={}",
                        referenceId, mediaId, sortOrder);

            } else if ("Room_Image".equalsIgnoreCase(category)) {
                // 儲存到 PETEL_ROOM_IMAGE
                Integer sortOrder;
                if (frontendSortOrder != null && frontendSortOrder > 0) {
                    // 優先使用前端指定的 sortOrder
                    sortOrder = frontendSortOrder;
                    log.info("[MEDIA-001] 使用前端指定的 sortOrder: {}", sortOrder);
                } else {
                    // 自動計算 sortOrder
                    Integer maxSortOrder = roomImageRepository.findMaxSortOrderByRoomId(referenceId);
                    sortOrder = (maxSortOrder != null ? maxSortOrder : 0) + 1;
                    log.info("[MEDIA-001] 自動計算 sortOrder: {}", sortOrder);
                }

                RoomImageEntity roomImage = new RoomImageEntity();
                roomImage.setRoomId(referenceId);
                roomImage.setMediaId(mediaId);
                roomImage.setSortOrder(sortOrder);

                roomImageRepository.save(roomImage);
                log.info("[MEDIA-001] PETEL_ROOM_IMAGE 記錄建立成功，RoomID={}, MediaID={}, SortOrder={}",
                        referenceId, mediaId, sortOrder);

            } else {
                log.info("[MEDIA-001] Category={} 不需要儲存到關聯表", category);
            }
        } catch (Exception e) {
            log.error("[MEDIA-001] 儲存關聯表失敗：{}", e.getMessage(), e);
            // 這裡不拋出異常，因為主要的媒體資料已經儲存成功
            // 可以根據業務需求決定是否要拋出異常
        }
    }

    /**
     * 生成媒體ID (M000000001 格式)
     * 格式：M + 9位數字
     * @return 媒體ID
     */
    private synchronized String generateMediaId() {
        // 1. 查詢資料庫中最大的 ID
        List<MediaBase64Entity> allMedia = mediaBase64Repository.findAll();

        long maxNumber = 0;
        for (MediaBase64Entity media : allMedia) {
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
            log.warn("[MEDIA-001] 無法計算 Base64 大小，使用預設值 0");
            return 0L;
        }
    }
}
