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
    private final UsersRepository usersRepository;
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

        // Step 4: 查詢房型資訊（根據 petType 過濾，並處理庫存）
        List<HOTEL005TranrsRoom> rooms = loadRooms(propertyId, petTypeList, tranrq.getCheckIn(), tranrq.getCheckOut());

        // Step 5: 查詢評價（含用戶頭像）
        List<HOTEL005TranrsReview> reviews = loadReviews(propertyId);

        // Step 6: 計算平均評分
        Double avgRating = calculateAverageRating(reviews);

        // Step 7: 查詢設施
        List<HOTEL005TranrsFacility> facilities = loadFacilities(propertyId);

        // Step 8: 查詢 city 和 district
        Map<String, String> postalInfo = queryPostalInfo(property.getPostalCode());

        // Step 9: 組裝回應
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
        detail.setFacilities(facilities);

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
     * 載入房型資訊（根據 petType 過濾，並處理庫存）
     */
    private List<HOTEL005TranrsRoom> loadRooms(String propertyId, List<String> petTypeList, String checkIn, String checkOut) {
        try {
            // 準備查詢參數
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("propertyId", propertyId);
            paramMap.put("petTypeList", petTypeList);

            // 判斷是否有提供日期，選擇對應的 SQL 檔案
            boolean hasDateRange = StringUtils.isNotBlank(checkIn) && StringUtils.isNotBlank(checkOut);
            String sqlFile;

            if (hasDateRange) {
                // 有日期：查詢 inventory
                sqlFile = "HOTEL005_QUERY_ROOMS_WITH_INVENTORY.sql";
                paramMap.put("checkIn", checkIn);
                paramMap.put("checkOut", checkOut);
                log.info("[HOTEL-005] 有提供日期 {} ~ {}，使用 inventory 查詢", checkIn, checkOut);
            } else {
                // 無日期：直接使用 TOTAL_UNITS
                sqlFile = "HOTEL005_QUERY_ROOMS_WITHOUT_INVENTORY.sql";
                log.info("[HOTEL-005] 未提供日期，直接使用 TOTAL_UNITS");
            }

            // 執行 SQL 查詢
            String sql = sqlUtils.getDynamicQuerySQL(sqlFile, paramMap);
            log.info("[HOTEL-005] 執行房型查詢 SQL: {}", sql);

            List<Map<String, Object>> queryResult = sqlAction.queryForList(sql, paramMap);

            if (queryResult.isEmpty()) {
                log.warn("[HOTEL-005] propertyId={} 無符合 petType 的房型", propertyId);
                return new ArrayList<>();
            }

            // 組裝房型資訊
            List<HOTEL005TranrsRoom> rooms = new ArrayList<>();
            for (Map<String, Object> row : queryResult) {
                HOTEL005TranrsRoom room = new HOTEL005TranrsRoom();
                room.setName((String) row.get("ROOM_NAME"));
                room.setInfo((String) row.get("ROOM_INFO"));
                room.setRoomSize((String) row.get("ROOM_SIZE"));

                // 處理價格
                Object priceObj = row.get("PRICE");
                room.setBasePrice(priceObj != null ? ((Number) priceObj).intValue() : 0);

                // 處理庫存數量
                Object qtyObj = row.get("AVAILABLE_QTY");
                room.setTotalUnits(qtyObj != null ? ((Number) qtyObj).intValue() : 0);

                // 載入房型圖片
                String roomId = (String) row.get("ROOM_ID");
                List<HOTEL005TranrsImage> roomImages = loadRoomImages(roomId);
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
     * 載入評價（含用戶頭像）
     */
    private List<HOTEL005TranrsReview> loadReviews(String propertyId) {
        try {
            List<ReviewsEntity> reviewEntities = reviewsRepository.findByPropertyId(propertyId);

            // 收集所有 userId
            List<String> userIds = reviewEntities.stream()
                    .map(ReviewsEntity::getUserId)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());

            // 批量查詢用戶資訊
            Map<String, UsersEntity> userMap = new HashMap<>();
            if (!userIds.isEmpty()) {
                List<UsersEntity> users = usersRepository.findAllById(userIds);
                userMap = users.stream()
                        .collect(Collectors.toMap(UsersEntity::getId, user -> user));
            }

            // 收集所有 mediaId
            List<String> mediaIds = userMap.values().stream()
                    .map(UsersEntity::getMediaId)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());

            // 批量查詢頭像資料
            Map<String, MediaBase64Entity> mediaMap = new HashMap<>();
            if (!mediaIds.isEmpty()) {
                List<MediaBase64Entity> mediaEntities = mediaBase64Repository.findAllById(mediaIds);
                mediaMap = mediaEntities.stream()
                        .collect(Collectors.toMap(MediaBase64Entity::getId, media -> media));
            }

            // 組裝評價資訊
            List<HOTEL005TranrsReview> reviews = new ArrayList<>();
            for (ReviewsEntity entity : reviewEntities) {
                HOTEL005TranrsReview review = new HOTEL005TranrsReview();
                review.setUserId(entity.getUserId());
                review.setPriceScore(entity.getPriceScore());
                review.setEnvScore(entity.getEnvScore());
                review.setServiceScore(entity.getServiceScore());
                review.setContent(entity.getContent());

                // 設定用戶名稱和頭像
                UsersEntity user = userMap.get(entity.getUserId());
                if (user != null) {
                    // 設定用戶名稱
                    review.setUserName(user.getName());

                    // 設定用戶頭像
                    if (StringUtils.isNotBlank(user.getMediaId())) {
                        MediaBase64Entity media = mediaMap.get(user.getMediaId());
                        if (media != null) {
                            HOTEL005TranrsImage avatar = new HOTEL005TranrsImage();
                            avatar.setMediaId(media.getId());
                            avatar.setBase64Data(media.getBase64Data());
                            avatar.setFileName(media.getFileName());
                            avatar.setMimeType(media.getMimeType());
                            avatar.setSortOrder(null);
                            review.setUserAvatar(avatar);
                        }
                    }
                }

                reviews.add(review);
            }

            return reviews;
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
     * 載入設施資訊
     */
    private List<HOTEL005TranrsFacility> loadFacilities(String propertyId) {
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("propertyId", propertyId);

            String sql = sqlUtils.getDynamicQuerySQL("HOTEL005_QUERY_FACILITIES.sql", paramMap);
            List<Map<String, Object>> queryResult = sqlAction.queryForList(sql, paramMap);

            return queryResult.stream()
                    .map(row -> {
                        HOTEL005TranrsFacility facility = new HOTEL005TranrsFacility();
                        facility.setFacilityId((String) row.get("FACILITY_ID"));
                        facility.setFacilityName((String) row.get("FACILITY_NAME"));
                        return facility;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[HOTEL-005] 查詢設施失敗", e);
            return new ArrayList<>();
        }
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
