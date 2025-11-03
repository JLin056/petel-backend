package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.*;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.*;
import com.example.petel.service.MEDIA004Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MEDIA-004 Base64 圖片查詢 Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MEDIA004SvcImpl implements MEDIA004Svc {

    private final MediaBase64Repository mediaBase64Repository;
    private final PropertyImageRepository propertyImageRepository;
    private final RoomImageRepository roomImageRepository;
    private final UsersRepository usersRepository;
    private final SellersRepository sellersRepository;

    /**
     * 查詢 Base64 圖片資料 (支援多種查詢方式)
     * @param req Base64 圖片查詢請求
     * @return 查詢結果
     */
    @Override
    public Res<MEDIA004Tranrs> queryBase64Media(Req<MEDIA004Tranrq> req) {
        log.info("-------- [MEDIA-004] Base64 圖片查詢 ---------");

        MEDIA004Tranrq tranrq = req.getTranrq();
        List<MEDIA004TranrsMediaInfo> mediaInfos = new ArrayList<>();

        // 1. 優先使用 mediaIds 查詢
        if (tranrq.getMediaIds() != null && !tranrq.getMediaIds().isEmpty()) {
            log.info("[MEDIA-004] 依 mediaIds 查詢，數量：{}", tranrq.getMediaIds().size());
            for (String mediaId : tranrq.getMediaIds()) {
                Optional<MediaBase64Entity> mediaOptional = mediaBase64Repository.findById(mediaId);
                if (mediaOptional.isPresent()) {
                    mediaInfos.add(convertToMediaInfo(mediaOptional.get(), null));
                }
            }

        } else if (StringUtils.isNotBlank(tranrq.getPropertyId())) {
            // 2. 使用 propertyId 查詢 (帶 sortOrder)
            log.info("[MEDIA-004] 依 propertyId 查詢：{}", tranrq.getPropertyId());
            mediaInfos = queryByPropertyIdWithSort(tranrq.getPropertyId());

        } else if (StringUtils.isNotBlank(tranrq.getRoomId())) {
            // 3. 使用 roomId 查詢 (帶 sortOrder)
            log.info("[MEDIA-004] 依 roomId 查詢：{}", tranrq.getRoomId());
            mediaInfos = queryByRoomIdWithSort(tranrq.getRoomId());

        } else if (StringUtils.isNotBlank(tranrq.getAccountId())) {
            // 4. 使用 accountId 查詢
            log.info("[MEDIA-004] 依 accountId 查詢：{}", tranrq.getAccountId());
            List<MediaBase64Entity> mediaEntities = queryByAccountId(tranrq.getAccountId());
            mediaInfos = mediaEntities.stream()
                    .map(entity -> convertToMediaInfo(entity, null))
                    .collect(Collectors.toList());

        } else if (tranrq.getBucket() != null && !tranrq.getBucket().trim().isEmpty()) {
            // 5. 使用 bucket 查詢
            log.info("[MEDIA-004] 依 bucket 查詢：{}", tranrq.getBucket());
            List<MediaBase64Entity> mediaEntities = mediaBase64Repository.findByBucket(tranrq.getBucket());
            mediaInfos = mediaEntities.stream()
                    .map(entity -> convertToMediaInfo(entity, null))
                    .collect(Collectors.toList());

        } else {
            // 6. 查詢全部
            log.info("[MEDIA-004] 查詢全部媒體");
            List<MediaBase64Entity> mediaEntities = mediaBase64Repository.findAll();
            mediaInfos = mediaEntities.stream()
                    .map(entity -> convertToMediaInfo(entity, null))
                    .collect(Collectors.toList());
        }

        log.info("[MEDIA-004] 查詢到 {} 筆媒體", mediaInfos.size());

        // 建立回應
        MEDIA004Tranrs tranrs = new MEDIA004Tranrs(
                mediaInfos.size(),
                mediaInfos
        );

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }

    /**
     * 轉換 MediaBase64Entity 為 MEDIA004TranrsMediaInfo
     */
    private MEDIA004TranrsMediaInfo convertToMediaInfo(MediaBase64Entity entity, Integer sortOrder) {
        MEDIA004TranrsMediaInfo mediaInfo = new MEDIA004TranrsMediaInfo();
        mediaInfo.setMediaId(entity.getId());
        mediaInfo.setBase64Data(entity.getBase64Data());
        mediaInfo.setBucket(entity.getBucket());
        mediaInfo.setFileName(entity.getFileName());
        mediaInfo.setSizeBytes(entity.getSizeBytes());
        mediaInfo.setMimeType(entity.getMimeType());
        mediaInfo.setCreatedAt(entity.getCreatedAt());
        mediaInfo.setUpdatedAt(entity.getUpdatedAt());
        mediaInfo.setSortOrder(sortOrder);
        return mediaInfo;
    }

    /**
     * 根據 propertyId 查詢圖片 (帶 sortOrder)
     */
    private List<MEDIA004TranrsMediaInfo> queryByPropertyIdWithSort(String propertyId) {
        try {
            // 1. 從 PETEL_PROPERTY_IMAGE 查詢 mediaId 和 sortOrder
            List<PropertyImageEntity> propertyImages = propertyImageRepository.findByPropertyId(propertyId);

            if (propertyImages.isEmpty()) {
                log.warn("[MEDIA-004] propertyId={} 查無圖片", propertyId);
                return new ArrayList<>();
            }

            // 2. 收集所有 mediaId
            List<String> mediaIds = propertyImages.stream()
                    .map(PropertyImageEntity::getMediaId)
                    .collect(Collectors.toList());

            // 3. 從 PETEL_MEDIA_BASE64 查詢圖片資料
            List<MediaBase64Entity> mediaEntities = mediaBase64Repository.findAllById(mediaIds);

            // 4. 建立 mediaId -> MediaBase64Entity 的 Map
            Map<String, MediaBase64Entity> mediaMap = mediaEntities.stream()
                    .collect(Collectors.toMap(MediaBase64Entity::getId, entity -> entity));

            // 5. 組裝結果，包含 sortOrder
            List<MEDIA004TranrsMediaInfo> result = new ArrayList<>();
            for (PropertyImageEntity propertyImage : propertyImages) {
                MediaBase64Entity media = mediaMap.get(propertyImage.getMediaId());
                if (media != null) {
                    result.add(convertToMediaInfo(media, propertyImage.getSortOrder()));
                }
            }

            // 6. 按 sortOrder 排序
            result.sort(Comparator.comparing(
                    MEDIA004TranrsMediaInfo::getSortOrder,
                    Comparator.nullsLast(Comparator.naturalOrder())
            ));

            return result;
        } catch (Exception e) {
            log.error("[MEDIA-004] 依 propertyId 查詢失敗", e);
            return new ArrayList<>();
        }
    }

    /**
     * 根據 propertyId 查詢圖片 (舊版，保留向後兼容)
     */
    private List<MediaBase64Entity> queryByPropertyId(String propertyId) {
        try {
            // 1. 從 PETEL_PROPERTY_IMAGE 查詢 mediaId 列表
            List<PropertyImageEntity> propertyImages = propertyImageRepository.findByPropertyId(propertyId);

            if (propertyImages.isEmpty()) {
                log.warn("[MEDIA-004] propertyId={} 查無圖片", propertyId);
                return new ArrayList<>();
            }

            // 2. 收集所有 mediaId
            List<String> mediaIds = propertyImages.stream()
                    .map(PropertyImageEntity::getMediaId)
                    .collect(Collectors.toList());

            // 3. 從 PETEL_MEDIA_BASE64 查詢圖片資料
            return mediaBase64Repository.findAllById(mediaIds);
        } catch (Exception e) {
            log.error("[MEDIA-004] 依 propertyId 查詢失敗", e);
            return new ArrayList<>();
        }
    }

    /**
     * 根據 roomId 查詢圖片 (帶 sortOrder)
     */
    private List<MEDIA004TranrsMediaInfo> queryByRoomIdWithSort(String roomId) {
        try {
            // 1. 從 PETEL_ROOM_IMAGE 查詢 mediaId 和 sortOrder
            List<RoomImageEntity> roomImages = roomImageRepository.findByRoomId(roomId);

            if (roomImages.isEmpty()) {
                log.warn("[MEDIA-004] roomId={} 查無圖片", roomId);
                return new ArrayList<>();
            }

            // 2. 收集所有 mediaId
            List<String> mediaIds = roomImages.stream()
                    .map(RoomImageEntity::getMediaId)
                    .collect(Collectors.toList());

            // 3. 從 PETEL_MEDIA_BASE64 查詢圖片資料
            List<MediaBase64Entity> mediaEntities = mediaBase64Repository.findAllById(mediaIds);

            // 4. 建立 mediaId -> MediaBase64Entity 的 Map
            Map<String, MediaBase64Entity> mediaMap = mediaEntities.stream()
                    .collect(Collectors.toMap(MediaBase64Entity::getId, entity -> entity));

            // 5. 組裝結果，包含 sortOrder
            List<MEDIA004TranrsMediaInfo> result = new ArrayList<>();
            for (RoomImageEntity roomImage : roomImages) {
                MediaBase64Entity media = mediaMap.get(roomImage.getMediaId());
                if (media != null) {
                    result.add(convertToMediaInfo(media, roomImage.getSortOrder()));
                }
            }

            // 6. 按 sortOrder 排序
            result.sort(Comparator.comparing(
                    MEDIA004TranrsMediaInfo::getSortOrder,
                    Comparator.nullsLast(Comparator.naturalOrder())
            ));

            return result;
        } catch (Exception e) {
            log.error("[MEDIA-004] 依 roomId 查詢失敗", e);
            return new ArrayList<>();
        }
    }

    /**
     * 根據 roomId 查詢圖片 (舊版，保留向後兼容)
     */
    private List<MediaBase64Entity> queryByRoomId(String roomId) {
        try {
            // 1. 從 PETEL_ROOM_IMAGE 查詢 mediaId 列表
            List<RoomImageEntity> roomImages = roomImageRepository.findByRoomId(roomId);

            if (roomImages.isEmpty()) {
                log.warn("[MEDIA-004] roomId={} 查無圖片", roomId);
                return new ArrayList<>();
            }

            // 2. 收集所有 mediaId
            List<String> mediaIds = roomImages.stream()
                    .map(RoomImageEntity::getMediaId)
                    .collect(Collectors.toList());

            // 3. 從 PETEL_MEDIA_BASE64 查詢圖片資料
            return mediaBase64Repository.findAllById(mediaIds);
        } catch (Exception e) {
            log.error("[MEDIA-004] 依 roomId 查詢失敗", e);
            return new ArrayList<>();
        }
    }

    /**
     * 根據 accountId 查詢圖片（先查 USERS，再查 SELLERS）
     */
    private List<MediaBase64Entity> queryByAccountId(String accountId) {
        try {
            String mediaId = null;

            // 1. 先從 PETEL_USERS 查詢 mediaId
            mediaId = usersRepository.findMediaIdByAccountId(accountId);
            if (StringUtils.isNotBlank(mediaId)) {
                log.info("[MEDIA-004] 從 USERS 表找到 accountId={}, mediaId={}", accountId, mediaId);
            }

            // 2. 如果 USERS 查不到，從 PETEL_SELLERS 查詢 mediaId
            if (StringUtils.isBlank(mediaId)) {
                mediaId = sellersRepository.findMediaIdByAccountId(accountId);
                if (StringUtils.isNotBlank(mediaId)) {
                    log.info("[MEDIA-004] 從 SELLERS 表找到 accountId={}, mediaId={}", accountId, mediaId);
                }
            }

            // 3. 如果都查不到
            if (StringUtils.isBlank(mediaId)) {
                log.warn("[MEDIA-004] accountId={} 在 USERS 和 SELLERS 表都查無資料", accountId);
                return new ArrayList<>();
            }

            // 4. 從 PETEL_MEDIA_BASE64 查詢圖片資料
            Optional<MediaBase64Entity> mediaOptional = mediaBase64Repository.findById(mediaId);
            if (mediaOptional.isPresent()) {
                List<MediaBase64Entity> result = new ArrayList<>();
                result.add(mediaOptional.get());
                return result;
            } else {
                log.warn("[MEDIA-004] mediaId={} 在 MEDIA_BASE64 表查無資料", mediaId);
                return new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("[MEDIA-004] 依 accountId 查詢失敗", e);
            return new ArrayList<>();
        }
    }
}
