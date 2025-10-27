package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.ADMIN003Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ADMIN003SvcImpl implements ADMIN003Svc {

    private final SqlUtils sqlUtils;
    private final SqlAction sqlAction;

    /**
     * 查詢訂單列表
     * @param req Req<ADMIN003Tranrq>
     * @return Res<ADMIN003Tranrs>
     * @throws DataNotFoundException 查無資料
     */
    @Override
    public Res<ADMIN003Tranrs> queryOrders(Req<ADMIN003Tranrq> req) throws DataNotFoundException, IOException {
        log.info("-------- [ADMIN-003] 查詢訂單列表 ---------");
        ADMIN003Tranrq tranrq = req.getTranrq();

        // 計算分頁參數
        PageRequest page = tranrq.getPage();


        int pageNumber = (page != null && page.getPageNumber() != null) ? page.getPageNumber() : 1;
        int pageSize = (page != null && page.getPageSize() != null) ? page.getPageSize() : 10;
        int offset = (pageNumber - 1) * pageSize;

        log.info("[ADMIN-003] pageNumber: {}", page.getPageNumber());
        log.info("[ADMIN-003] pageSize: {}", page.getPageSize());

        // 建立查詢參數 Map
        Map<String, Object> paramMap = new HashMap<>();



        // 動態參數（全部使用模糊查詢）
        if (StringUtils.isNotBlank(tranrq.getOrderId())) {
            paramMap.put("orderId", "%" + tranrq.getOrderId() + "%");
        }
        if (StringUtils.isNotBlank(tranrq.getCheckIn())) {
            paramMap.put("checkIn", "%" + tranrq.getCheckIn() + "%");
        }
        if (StringUtils.isNotBlank(tranrq.getUserName())) {
            paramMap.put("userName", "%" + tranrq.getUserName() + "%");
        }
        if (StringUtils.isNotBlank(tranrq.getUserPhone())) {
            paramMap.put("userPhone", "%" + tranrq.getUserPhone() + "%");
        }
        if (StringUtils.isNotBlank(tranrq.getPropertyName())) {
            paramMap.put("propertyName", "%" + tranrq.getPropertyName() + "%");
        }
        if (StringUtils.isNotBlank(tranrq.getPropertyPhone())) {
            paramMap.put("propertyPhone", "%" + tranrq.getPropertyPhone() + "%");
        }


        log.info("[ADMIN-003] 查詢參數: {}", paramMap);

        // SqlUtils 會自動處理 SQL 中的 [] 條件
        String sql = sqlUtils.getDynamicQuerySQL("ADMIN003_Query.sql", paramMap);
        String countSql = sqlUtils.getDynamicQuerySQL("ADMIN003_Count.sql", paramMap);



        log.info("[ADMIN-003] COUNT 實際使用的參數: {}", paramMap);

        // 查詢總筆數
        List<Map<String, Object>> countResult = sqlAction.queryForList(countSql, paramMap);
        Integer totalCount = 0;
        if (countResult != null && !countResult.isEmpty()) {
            Object countObj = countResult.get(0).get("TOTAL_COUNT");
            totalCount = countObj != null ? ((Number) countObj).intValue() : 0;
        }

        // 檢查是否有資料
        if (totalCount == null || totalCount == 0) {
            log.warn("[ADMIN-003] 查無訂單資料");
            throw new DataNotFoundException("查無訂單資料");
        }

        // 計算總頁數
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 加入分頁參數
        paramMap.put("offset", offset);
        paramMap.put("pageNumber", pageSize);

        log.info("[ADMIN-003] QUERY 實際使用的參數: {}", paramMap);

        // 執行查詢 - 取得原始資料
        List<ADMIN003QueryResult> queryResults = sqlAction.queryForListVO(
                sql,
                paramMap,
                ADMIN003QueryResult.class,
                true
        );

        // 在 Java 中處理 GUEST 邏輯，轉換成回傳的 DTO
        List<ADMIN003TranrsTranrs> orders = new ArrayList<>();

        // 取得查詢條件 (移除 LIKE 的 % 符號以便精確比對)
        String queryUserName = (String) paramMap.get("userName");
        if (StringUtils.isNotBlank(queryUserName)) {
            queryUserName = queryUserName.replace("%", "");
        }
        String queryUserPhone = (String) paramMap.get("userPhone");

        for (ADMIN003QueryResult result : queryResults) {
            // 在 Java 中判斷是否為代訂
            // GUEST='n' 表示代訂，顯示 GUEST_NAME 和 GUEST_PHONE
            // GUEST='y' 表示訂購人是會員本人，顯示 USER_NAME 和 USER_PHONE
            String finalUserName;
            String finalUserPhone;

            if ("n".equals(result.getGuest())) {
                finalUserName = result.getGuestName();
                finalUserPhone = result.getGuestPhone();
            } else {
                finalUserName = result.getUserName();
                finalUserPhone = result.getUserPhone();
            }

            // 如果有 userName 查詢條件，檢查最終的 userName 是否包含查詢字串
            if (StringUtils.isNotBlank(queryUserName)) {
                if (finalUserName == null || !finalUserName.contains(queryUserName)) {
                    log.debug("[ADMIN-003] 過濾掉不符合的訂單: ORDER_ID={}, finalUserName={}, queryUserName={}",
                            result.getOrderId(), finalUserName, queryUserName);
                    continue; // 跳過這筆資料
                }
            }

            // 如果有 userPhone 查詢條件，檢查最終的 userPhone 是否完全吻合
            if (StringUtils.isNotBlank(queryUserPhone)) {
                if (!queryUserPhone.equals(finalUserPhone)) {
                    log.debug("[ADMIN-003] 過濾掉不符合的訂單: ORDER_ID={}, finalUserPhone={}, queryUserPhone={}",
                            result.getOrderId(), finalUserPhone, queryUserPhone);
                    continue; // 跳過這筆資料
                }
            }

            ADMIN003TranrsTranrs order = new ADMIN003TranrsTranrs();
            order.setOrderId(result.getOrderId());
//            order.setStayDate(result.getStayDate());
            order.setCheckIn(result.getCheckIn());
            order.setCheckOut(result.getCheckOut());
            order.setUserName(result.getUserName());
            order.setUserPhone(result.getUserPhone());
            order.setFinalUserName(finalUserName);
            order.setFinalUserPhone(finalUserPhone);
            order.setPropertyName(result.getPropertyName());
            order.setPropertyPhone(result.getPropertyPhone());
            order.setRoom(result.getRoom());
//            order.setQuantity(result.getQuantity());
            order.setHotelCharges(result.getHotelCharges());
//            order.setPriceEverynight(result.getPriceEverynight());
            order.setStatus(result.getStatus());
            order.setNote(result.getNote());
            order.setCreatedAt(result.getCreatedAt());
            order.setUpdatedAt(result.getUpdatedAt());

            orders.add(order);
        }

        log.info("[ADMIN-003] 查詢成功，共 {} 筆訂單，當前頁 {}/{}", totalCount, pageNumber, totalPages);

        // 組裝回應
        ADMIN003Tranrs tranrs = new ADMIN003Tranrs();
        tranrs.setOrders(orders);
        tranrs.setTotalCount(totalCount);
        tranrs.setTotalPages(totalPages);
        tranrs.setCurrentPage(pageNumber);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
