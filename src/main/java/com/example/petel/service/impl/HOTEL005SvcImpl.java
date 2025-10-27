package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.repository.*;
import com.example.petel.service.HOTEL005Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HOTEL005SvcImpl implements HOTEL005Svc {

    private final PropertyRepository propertyRepository;
    private final RoomsRepository roomsRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final RoomImageRepository roomImageRepository;
    private final MediaBase64Repository mediaBase64Repository;
    private final ReviewsRepository reviewsRepository;
    private final SqlUtils sqlUtils;
    private final SqlAction sqlAction;

    @Override
    public Res<HOTEL005Tranrs> singleHotelDetail(Req<HOTEL005Tranrq> hotel005Tranrq)
            throws DataNotFoundException, InvalidInputException {
        log.info("-------- [HOTEL-005] 查詢單筆旅館詳細資訊 ---------");

        HOTEL005Tranrq tranrq = hotel005Tranrq.getTranrq();
        String propertyId = tranrq.getPropertyId();
        String petType = tranrq.getPetType();

        log.info("[HOTEL-005] propertyId={}, petType={}", propertyId, petType);

        // Step 1: 查詢旅館基本資訊
        PropertyEntity property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> {
                    log.error("[HOTEL-005] 查無 propertyId={} 的旅館", propertyId);
                    return new DataNotFoundException("查無旅館資料");
                });

        // Step 2: 處理 petType 參數，轉換成 petTypeList
        List<String> petTypeList = convertPetTypeToList(petType);

        // Step 3: 查詢旅館圖片
        List<HOTEL005TranrsImage> propertyImages = loadPropertyImages(propertyId);

        // Step 4: 查詢房型資訊（根據 petType 過濾）
        List<HOTEL005TranrsRoom> rooms = loadRooms(propertyId, petTypeList);

        // Step 5: 查詢評價
        List<HOTEL005TranrsReview> reviews = loadReviews(propertyId);

        // Step 6: 計算平均評分
        Double avgRating = calculateAverageRating(reviews);

        // Step 7: 查詢 city 和 district
        Map<String, String> postalInfo = queryPostalInfo(property.getPostalCode());

        // Step 8: 組裝回應
        HOTEL005TranrsSingleHotelDetail detail = new HOTEL005TranrsSingleHotelDetail();
        detail.setName(property.getName());
        detail.setAddress(property.getAddress());
        detail.setInfo(property.getInfo());
        detail.setAvgRating(avgRating);
        detail.setReviewCount(reviews.size());
        detail.setRoomCount(rooms.size());
        detail.setCity(postalInfo.get("CITY"));
        detail.setDistrict(postalInfo.get("DISTRICT"));
        detail.setPropertyImages(propertyImages);
        detail.setRooms(rooms);
        detail.setReviews(reviews);

        HOTEL005Tranrs tranrs = new HOTEL005Tranrs();
        tranrs.setSingleHotelDetail(detail);

        log.info("[HOTEL-005] 查詢成功");

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }

    /**
     * 將 petType (CAT/DOG) 轉換成 petTypeList
     */
    private List<String> convertPetTypeToList(String petType) {
        if ("CAT".equalsIgnoreCase(petType)) {
            return Collections.singletonList("W001");
        } else if ("DOG".equalsIgnoreCase(petType)) {
            return Arrays.asList("W002", "W003", "W004", "W005", "W006");
        } else {
            // 如果直接傳入 W001, W002 等，則使用單一類型查詢
            return Collections.singletonList(petType);
        }
    }

    /**
     * 載入旅館圖片
     */
    private List<HOTEL005TranrsImage> loadPropertyImages(String propertyId) {
        try {
            List<PropertyImageEntity> imageEntities = propertyImageRepository.findByPropertyId(propertyId);
            if (imageEntities.isEmpty()) {
                return new ArrayList<>();
            }

            // 收集所有 mediaId
            List<String> mediaIds = imageEntities.stream()
                    .map(PropertyImageEntity::getMediaId)
                    .collect(Collectors.toList());

            // 批量查詢 MediaBase64
            List<MediaBase64Entity> mediaEntities = mediaBase64Repository.findAllById(mediaIds);
            Map<String, MediaBase64Entity> mediaMap = mediaEntities.stream()
                    .collect(Collectors.toMap(MediaBase64Entity::getId, entity -> entity));

            // 組裝圖片資訊
            List<HOTEL005TranrsImage> images = new ArrayList<>();
            for (PropertyImageEntity imageEntity : imageEntities) {
                MediaBase64Entity media = mediaMap.get(imageEntity.getMediaId());
                if (media != null) {
                    HOTEL005TranrsImage image = new HOTEL005TranrsImage();
                    image.setMediaId(media.getId());
                    image.setBase64Data(media.getBase64Data());
                    image.setFileName(media.getFileName());
                    image.setMimeType(media.getMimeType());
                    image.setSortOrder(imageEntity.getSortOrder());
                    images.add(image);
                }
            }

            // 按 sortOrder 排序
            images.sort(Comparator.comparingInt(HOTEL005TranrsImage::getSortOrder));

            return images;
        } catch (Exception e) {
            log.error("[HOTEL-005] 查詢旅館圖片失敗", e);
            return new ArrayList<>();
        }
    }

    /**
     * 載入房型資訊（根據 petType 過濾）
     */
    private List<HOTEL005TranrsRoom> loadRooms(String propertyId, List<String> petTypeList) {
        try {
            // 查詢所有房型
            List<RoomsEntity> allRooms = roomsRepository.findByPropertyId(propertyId);

            // 根據 petTypeList 過濾
            List<RoomsEntity> filteredRooms = allRooms.stream()
                    .filter(room -> petTypeList.contains(room.getPetTypeId()))
                    .collect(Collectors.toList());

            if (filteredRooms.isEmpty()) {
                log.warn("[HOTEL-005] propertyId={} 無符合 petType 的房型", propertyId);
                return new ArrayList<>();
            }

            // 組裝房型資訊
            List<HOTEL005TranrsRoom> rooms = new ArrayList<>();
            for (RoomsEntity roomEntity : filteredRooms) {
                HOTEL005TranrsRoom room = new HOTEL005TranrsRoom();
                room.setName(roomEntity.getName());
                room.setInfo(roomEntity.getInfo());
                room.setBasePrice(roomEntity.getBasePrice());
                room.setTotalUnits(roomEntity.getTotalUnits());

                // 載入房型圖片
                List<HOTEL005TranrsImage> roomImages = loadRoomImages(roomEntity.getId());
                room.setRoomImages(roomImages);

                rooms.add(room);
            }

            return rooms;
        } catch (Exception e) {
            log.error("[HOTEL-005] 查詢房型失敗", e);
            return new ArrayList<>();
        }
    }

    /**
     * 載入房型圖片
     */
    private List<HOTEL005TranrsImage> loadRoomImages(String roomId) {
        try {
            List<RoomImageEntity> imageEntities = roomImageRepository.findByRoomId(roomId);
            if (imageEntities.isEmpty()) {
                return new ArrayList<>();
            }

            // 收集所有 mediaId
            List<String> mediaIds = imageEntities.stream()
                    .map(RoomImageEntity::getMediaId)
                    .collect(Collectors.toList());

            // 批量查詢 MediaBase64
            List<MediaBase64Entity> mediaEntities = mediaBase64Repository.findAllById(mediaIds);
            Map<String, MediaBase64Entity> mediaMap = mediaEntities.stream()
                    .collect(Collectors.toMap(MediaBase64Entity::getId, entity -> entity));

            // 組裝圖片資訊
            List<HOTEL005TranrsImage> images = new ArrayList<>();
            for (RoomImageEntity imageEntity : imageEntities) {
                MediaBase64Entity media = mediaMap.get(imageEntity.getMediaId());
                if (media != null) {
                    HOTEL005TranrsImage image = new HOTEL005TranrsImage();
                    image.setMediaId(media.getId());
                    image.setBase64Data(media.getBase64Data());
                    image.setFileName(media.getFileName());
                    image.setMimeType(media.getMimeType());
                    image.setSortOrder(imageEntity.getSortOrder());
                    images.add(image);
                }
            }

            // 按 sortOrder 排序
            images.sort(Comparator.comparingInt(HOTEL005TranrsImage::getSortOrder));

            return images;
        } catch (Exception e) {
            log.error("[HOTEL-005] 查詢房型圖片失敗", e);
            return new ArrayList<>();
        }
    }

    /**
     * 載入評價
     */
    private List<HOTEL005TranrsReview> loadReviews(String propertyId) {
        try {
            List<ReviewsEntity> reviewEntities = reviewsRepository.findByPropertyId(propertyId);

            return reviewEntities.stream()
                    .map(entity -> {
                        HOTEL005TranrsReview review = new HOTEL005TranrsReview();
                        review.setPriceScore(entity.getPriceScore());
                        review.setEnvScore(entity.getEnvScore());
                        review.setServiceScore(entity.getServiceScore());
                        review.setContent(entity.getContent());
                        return review;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[HOTEL-005] 查詢評價失敗", e);
            return new ArrayList<>();
        }
    }

    /**
     * 計算平均評分
     */
    private Double calculateAverageRating(List<HOTEL005TranrsReview> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        double totalRating = reviews.stream()
                .mapToDouble(review -> (review.getPriceScore() + review.getEnvScore() + review.getServiceScore()) / 3.0)
                .sum();

        return Math.round(totalRating / reviews.size() * 100.0) / 100.0;
    }

    /**
     * 查詢郵遞區號資訊（city 和 district）
     */
    private Map<String, String> queryPostalInfo(String postalCode) {
        Map<String, String> result = new HashMap<>();
        result.put("CITY", null);
        result.put("DISTRICT", null);

        if (StringUtils.isBlank(postalCode)) {
            return result;
        }

        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("postalCode", postalCode);

            String sql = sqlUtils.getDynamicQuerySQL("HOTEL005_QUERY_POSTAL.sql", paramMap);
            List<Map<String, Object>> queryResult = sqlAction.queryForList(sql, paramMap);

            if (!queryResult.isEmpty()) {
                Map<String, Object> row = queryResult.get(0);
                result.put("CITY", (String) row.get("CITY"));
                result.put("DISTRICT", (String) row.get("DISTRICT"));
            }
        } catch (IOException e) {
            log.error("[HOTEL-005] 查詢郵遞區號資訊失敗", e);
        }

        return result;
    }
}
