package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.ErrorInputException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.MERCH003Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH003SvcImpl implements MERCH003Svc {

    /**
     * MERCH001_QUERY
     */
    private static final String MERCH003_QUERY = "MERCH003_QUERY.sql";
    /**
     * SqlAction
     */
    private final SqlAction sqlAction;
    /**
     * SqlUtilis
     */
    private final SqlUtils sqlUtils;

    /**
     * 評價列表
     *
     * @param merch003Tranrq Req<MERCH003Tranrq> （propertyId)
     * @return Res<MERCH003Tranrs>
     * @throws DataNotFoundException, IOException
     */
    @Override
    public Res<MERCH003Tranrs<MERCH003TranrsReview>> reviews(Req<MERCH003Tranrq> merch003Tranrq) throws DataNotFoundException, IOException {
        log.info("-------- [MERCH-003] 評價列表 ---------");
        Map<String, Object> paramMap = new HashMap<>();

        String propertyId = merch003Tranrq.getTranrq().getPropertyId();
        log.info("[MERCH-003] 查詢 propertyId = {}", propertyId);
        paramMap.put("propertyId", propertyId);

        MERCH003TranrqPage pageData = merch003Tranrq.getTranrq().getPage();
        log.info("[MERCH-003] 查詢 pageData = {}", pageData);
        Integer pageSize = pageData.getPageSize();
        Integer pageNumber = pageData.getPageNumber();
        if (pageSize == null || pageSize <= 0) {
            log.warn("[MERCH-003] 分頁筆數錯誤");
            throw new ErrorInputException("分頁筆數錯誤:pageSize必須有值且>0");
        }
        if (pageNumber == null || pageNumber < 1) {
            log.warn("[MERCH-003] 頁碼錯誤");
            throw new ErrorInputException("頁碼錯誤:pageNumber必須有值且>1");
        }
        paramMap.put("pageSize", pageSize);
        paramMap.put("offset", (pageNumber - 1) * pageSize);

        List<MERCH003TranrsReview> reviewList = new ArrayList<>();

        String sql = sqlUtils.getQuerySql(MERCH003_QUERY);
        log.info("SQL = {}, params = {}", sql, paramMap);
        List<Map<String, Object>> mapList = sqlAction.queryForList(sql, paramMap);
        if (CollectionUtils.isEmpty(mapList)) {
            log.warn("[MERCH-003] 依據 propertyId 查無資料");
            throw new DataNotFoundException("查無訂單資料");
        }

        mapList.forEach(map -> {
            MERCH003TranrsReview review = new MERCH003TranrsReview();
            review.setOrderId(MapUtils.getString(map, "ORDER_ID"));
            review.setUserName(MapUtils.getString(map, "USER_NAME"));
            review.setPriceScore(MapUtils.getDouble(map, "PRICE_SCORE"));
            review.setEnvScore(MapUtils.getDouble(map, "ENV_SCORE"));
            review.setServiceScore(MapUtils.getDouble(map, "SERVICE_SCORE"));
            review.setContent(MapUtils.getString(map, "CONTENT"));
            Object createdAtObj = MapUtils.getObject(map, "CREATED_AT");
            if (createdAtObj instanceof java.sql.Timestamp timestamp) {
                review.setCreatedAt(timestamp.toLocalDateTime());
            } else if (createdAtObj instanceof java.time.LocalDateTime localDateTime) {
                review.setCreatedAt(localDateTime);
            }
            reviewList.add(review);
        });
        log.info("[MERCH-003] 查詢成功，propertyId={}, reviewList={}", propertyId, reviewList);

        int totalCount = MapUtils.getInteger(mapList.get(0), "TOTAL_COUNT");

        MERCH003Tranrs<MERCH003TranrsReview> merch003Tranrs = new MERCH003Tranrs<>();
        merch003Tranrs.setReviews(reviewList);
        merch003Tranrs.setPageNumber(pageData.getPageNumber());
        int totalPage = (int) Math.ceil(totalCount / pageData.getPageSize().doubleValue());
        merch003Tranrs.setTotalPage(totalPage);
        merch003Tranrs.setTotalCount(totalCount);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch003Tranrs
        );
    }
}