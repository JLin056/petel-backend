package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaBase64Entity;
import com.example.petel.entity.PropertyImageEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.repository.MediaBase64Repository;
import com.example.petel.repository.PropertyImageRepository;
import com.example.petel.service.HOTEL001Svc;
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
public class HOTEL001SvcImpl implements HOTEL001Svc {

    private final SqlUtils sqlUtils;
    private final SqlAction sqlAction;
    private final PropertyImageRepository propertyImageRepository;
    private final MediaBase64Repository mediaBase64Repository;

    /**
     * 查詢旅館列表
     * @param req Req<HOTEL001Tranrq>
     * @return Res<HOTEL001Tranrs<HOTEL001TranrsHotel>>
     */
    @Override
    public Res<HOTEL001Tranrs<HOTEL001TranrsHotel>> queryHotels(Req<HOTEL001Tranrq> req)
            throws DataNotFoundException, InvalidInputException {
        log.info("-------- [HOTEL-001] 查詢旅館列表 ---------");
        HOTEL001Tranrq tranrq = req.getTranrq();

        // 建立查詢參數 Map
        Map<String, Object> paramMap = buildParamMap(tranrq);

        // Step 1: 根據是否有提供時間，選擇對應的 SQL 檔案
        boolean hasDateRange = StringUtils.isNotBlank(tranrq.getCheckIn()) &&
                                StringUtils.isNotBlank(tranrq.getCheckOut());

        String countSqlFile;
        String querySqlFile;

        if (hasDateRange) {
            // 有提供時間：使用 WITH_INVENTORY SQL
            // SQL 會自動判斷：有 inventory 記錄用 AVAILABLE_QTY，沒有則用 TOTAL_UNITS
            log.info("[HOTEL-001] 有提供入住日期 {} ~ {}，使用庫存查詢（會自動判斷使用 AVAILABLE_QTY 或 TOTAL_UNITS）",
                    tranrq.getCheckIn(), tranrq.getCheckOut());
            countSqlFile = "HOTEL001_COUNT_WITH_INVENTORY.sql";
            querySqlFile = "HOTEL001_QUERY_WITH_INVENTORY.sql";
        } else {
            // 沒有提供時間：直接使用 TOTAL_UNITS
            log.info("[HOTEL-001] 未提供入住日期，直接使用 TOTAL_UNITS 查詢");
            countSqlFile = "HOTEL001_COUNT_WITHOUT_INVENTORY.sql";
            querySqlFile = "HOTEL001_QUERY_WITHOUT_INVENTORY.sql";
        }

        // Step 3: 查詢總筆數
        Integer totalCount = getTotalCount(countSqlFile, paramMap);
        if (totalCount == null || totalCount == 0) {
            log.warn("[HOTEL-001] 查無符合條件的旅館");
            throw new DataNotFoundException("查無符合條件的旅館");
        }

        // Step 4: 計算分頁參數（頁數從 1 開始）
        HOTEL001TranrqPage page = tranrq.getPage();
        int pageNumber = (page != null && page.getPageNumber() != null) ? page.getPageNumber() : 1;
        int pageSize = (page != null && page.getPageSize() != null) ? page.getPageSize() : 10;

        // 確保 pageNumber 最小為 1
        if (pageNumber < 1) {
            pageNumber = 1;
        }

        // 計算 offset（頁數從 1 開始，所以要減 1）
        int offset = (pageNumber - 1) * pageSize;
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 加入分頁參數
        paramMap.put("offset", offset);
        paramMap.put("pageSize", pageSize);

        // Step 5: 執行主查詢
        List<HOTEL001TranrsHotel> hotels = queryHotels(querySqlFile, paramMap);

        // Step 6: 批量查詢圖片
        if (!hotels.isEmpty()) {
            loadHotelImages(hotels);
        }

        log.info("[HOTEL-001] 查詢成功，共 {} 筆，當前頁 {}/{}", totalCount, pageNumber, totalPages);

        // 組裝回應
        HOTEL001Tranrs<HOTEL001TranrsHotel> tranrs = new HOTEL001Tranrs<>();
        tranrs.setHotels(hotels);
        tranrs.setTotalCount(totalCount);
        tranrs.setTotalPage(totalPages);
        tranrs.setPageNumber(pageNumber);
        tranrs.setPageSize(pageSize);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }

    /**
     * 建立查詢參數 Map
     */
    private Map<String, Object> buildParamMap(HOTEL001Tranrq tranrq) {
        Map<String, Object> paramMap = new HashMap<>();

        // 處理 petType 參數：CAT 或 DOG
        String petType = tranrq.getPetType();
        List<String> petTypeList;

        if ("CAT".equalsIgnoreCase(petType)) {
            // 貓：只查詢 W001
            petTypeList = Collections.singletonList("W001");
        } else if ("DOG".equalsIgnoreCase(petType)) {
            // 狗：查詢所有狗房型 W002, W003, W004, W005, W006
            petTypeList = Arrays.asList("W002", "W003", "W004", "W005", "W006");
        } else {
            // 如果直接傳入 W001, W002 等，則使用單一類型查詢
            petTypeList = Collections.singletonList(petType);
        }

        paramMap.put("petTypeList", petTypeList);

        // petCount 預設為 1
        int petCount = (tranrq.getPetCount() != null && tranrq.getPetCount() > 0) ?
                tranrq.getPetCount() : 1;
        paramMap.put("petCount", petCount);

        // 選填參數
        if (StringUtils.isNotBlank(tranrq.getCheckIn())) {
            paramMap.put("checkIn", tranrq.getCheckIn());
        }
        if (StringUtils.isNotBlank(tranrq.getCheckOut())) {
            paramMap.put("checkOut", tranrq.getCheckOut());
        }
        if (StringUtils.isNotBlank(tranrq.getCity())) {
            paramMap.put("city", tranrq.getCity());
        }
        if (tranrq.getPriceMin() != null) {
            paramMap.put("priceMin", tranrq.getPriceMin());
        }
        if (tranrq.getPriceMax() != null) {
            paramMap.put("priceMax", tranrq.getPriceMax());
        }
        if (tranrq.getMinRating() > 0) {
            paramMap.put("minRating", tranrq.getMinRating());
        }
        if (tranrq.getFacilities() != null && !tranrq.getFacilities().isEmpty()) {
            paramMap.put("facilities", tranrq.getFacilities());
            paramMap.put("facilityCount", tranrq.getFacilities().size());
        }

        return paramMap;
    }

    /**
     * 查詢總筆數
     */
    private Integer getTotalCount(String sqlFile, Map<String, Object> paramMap) throws DataNotFoundException {
        try {
            String sql = sqlUtils.getDynamicQuerySQL(sqlFile, paramMap);
            log.info("[HOTEL-001] 執行 COUNT SQL: {}", sql);

            List<Map<String, Object>> countResult = sqlAction.queryForList(sql, paramMap);
            if (countResult.isEmpty()) {
                return 0;
            }

            return ((Number) countResult.get(0).get("TOTAL_COUNT")).intValue();
        } catch (IOException e) {
            log.error("[HOTEL-001] 查詢總筆數失敗", e);
            throw new DataNotFoundException("系統錯誤：無法查詢旅館總數");
        }
    }

    /**
     * 執行主查詢
     */
    private List<HOTEL001TranrsHotel> queryHotels(String sqlFile, Map<String, Object> paramMap)
            throws DataNotFoundException {
        try {
            String sql = sqlUtils.getDynamicQuerySQL(sqlFile, paramMap);
            log.info("[HOTEL-001] 執行主查詢 SQL: {}", sql);

            return sqlAction.queryForListVO(
                    sql,
                    paramMap,
                    HOTEL001TranrsHotel.class,
                    true
            );
        } catch (IOException e) {
            log.error("[HOTEL-001] 查詢旅館列表失敗", e);
            throw new DataNotFoundException("系統錯誤：無法查詢旅館列表");
        }
    }

    /**
     * 批量查詢旅館封面照（sortOrder = 1）- 使用 JPA Repository
     */
    private void loadHotelImages(List<HOTEL001TranrsHotel> hotels) {
        try {
            // 收集所有 propertyId
            List<String> propertyIds = hotels.stream()
                    .map(HOTEL001TranrsHotel::getPropertyId)
                    .collect(Collectors.toList());

            log.info("[HOTEL-001] 開始查詢 {} 間旅館的封面照", propertyIds.size());

            // 步驟 1: 查詢所有旅館的圖片關聯記錄（PETEL_PROPERTY_IMAGE）
            List<PropertyImageEntity> coverImages = new ArrayList<>();
            for (String propertyId : propertyIds) {
                List<PropertyImageEntity> images = propertyImageRepository.findByPropertyId(propertyId);
                // 只取 sortOrder = 1 的封面照
                images.stream()
                        .filter(img -> img.getSortOrder() != null && img.getSortOrder() == 1)
                        .findFirst()
                        .ifPresent(coverImages::add);
            }

            if (coverImages.isEmpty()) {
                log.info("[HOTEL-001] 沒有找到任何封面照");
                hotels.forEach(hotel -> hotel.setImages(new ArrayList<>()));
                return;
            }

            log.info("[HOTEL-001] 找到 {} 個封面照", coverImages.size());

            // 步驟 2: 收集所有封面照的 mediaId
            List<String> mediaIds = coverImages.stream()
                    .map(PropertyImageEntity::getMediaId)
                    .collect(Collectors.toList());

            // 步驟 3: 批量查詢 PETEL_MEDIA_BASE64（JPA 會自動處理 CLOB 轉換）
            List<MediaBase64Entity> mediaEntities = mediaBase64Repository.findAllById(mediaIds);

            // 建立 mediaId -> MediaBase64Entity 的 Map
            Map<String, MediaBase64Entity> mediaMap = mediaEntities.stream()
                    .collect(Collectors.toMap(MediaBase64Entity::getId, entity -> entity));

            log.info("[HOTEL-001] 查詢到 {} 筆封面照資料", mediaEntities.size());

            // 步驟 4: 組裝封面照資訊（按 propertyId 分組）
            Map<String, HOTEL001TranrsHotelImage> coverImageMap = new HashMap<>();

            for (PropertyImageEntity propertyImage : coverImages) {
                String propertyId = propertyImage.getPropertyId();
                String mediaId = propertyImage.getMediaId();
                MediaBase64Entity mediaEntity = mediaMap.get(mediaId);

                if (mediaEntity != null) {
                    HOTEL001TranrsHotelImage imageInfo = new HOTEL001TranrsHotelImage();
                    imageInfo.setMediaId(mediaId);
                    imageInfo.setBase64Data(mediaEntity.getBase64Data());
                    imageInfo.setFileName(mediaEntity.getFileName());
                    imageInfo.setMimeType(mediaEntity.getMimeType());
                    imageInfo.setSortOrder(propertyImage.getSortOrder());

                    coverImageMap.put(propertyId, imageInfo);
                } else {
                    log.warn("[HOTEL-001] MediaID={} 在 PETEL_MEDIA_BASE64 中不存在", mediaId);
                }
            }

            // 步驟 5: 將封面照設定到對應的 hotel
            for (HOTEL001TranrsHotel hotel : hotels) {
                HOTEL001TranrsHotelImage coverImage = coverImageMap.get(hotel.getPropertyId());
                if (coverImage != null) {
                    hotel.setImages(Collections.singletonList(coverImage));
                } else {
                    hotel.setImages(new ArrayList<>());
                }
            }

            log.info("[HOTEL-001] 成功載入 {} 間旅館的封面照", hotels.size());

        } catch (Exception e) {
            log.error("[HOTEL-001] 查詢封面照失敗，使用空列表", e);
            // 查詢圖片失敗不影響主流程，設定空列表即可
            hotels.forEach(hotel -> hotel.setImages(new ArrayList<>()));
        }
    }
}
