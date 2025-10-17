package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.BOOK002Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BOOK-002 取得該筆訂單 SvcImpl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BOOK002SvcImpl implements BOOK002Svc {

    /** sqlFile */
    private static final String sqlFile = "BOOK002_QUERY.sql";
    /** sqlAction */
    private final SqlAction sqlAction;
    /** sqlUtils */
    private final SqlUtils sqlUtils;

    /**
     * 取得該筆訂單
     *
     * @param requestBody Req<BOOK002Tranrq>
     * @return Res<BOOK002Tranrs>
     * @throws DataNotFoundException 查無資料
     * @throws IOException           檔案讀寫異常
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<BOOK002Tranrs> book002(Req<BOOK002Tranrq> requestBody) throws DataNotFoundException, IOException {

        log.info("-------- [BOOK-002] 取得該筆訂單 API 啟動 --------");

        String orderId = requestBody.getTranrq().getOrderId();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderID", orderId);
        String sql = sqlUtils.getQuerySql(sqlFile);

        if (sql == null || sql.isBlank()) {
            log.error("[BOOK-002] SQL 檔內容為空或讀取失敗");
            throw new IOException();
        }

        List<Map<String, Object>> mapList = sqlAction.queryForList(sql, paramMap);

        if (mapList.isEmpty()) {
            log.error("[BOOK-002] 查無訂單編號為 {} 的訂單資料，訂單查詢失敗", orderId);
            throw new DataNotFoundException();
        }

        List<BOOKTranrqOrderDetail> orderDetails = new ArrayList<>();

        for (Map<String, Object> queryResult : mapList) {
            BOOKTranrqOrderDetail orderDetail = new BOOKTranrqOrderDetail();
            orderDetail.setRoomId(MapUtils.getString(queryResult, "ROOM_ID"));
            orderDetail.setArrivalDate(MapUtils.getString(queryResult, "ARRIVAL_DATE"));
            orderDetail.setRoomQuantity(MapUtils.getInteger(queryResult, "QUANTITY"));
            orderDetail.setRoomPrice(MapUtils.getInteger(queryResult, "PRICE"));
            orderDetails.add(orderDetail);
        }

        Map<String, Object> orderInfoMap = mapList.get(0);

        BOOK002Tranrs book002Tranrs = new BOOK002Tranrs();
        book002Tranrs.setOrderId(orderId);
        book002Tranrs.setUserId(MapUtils.getString(orderInfoMap, "USER_ID"));
        book002Tranrs.setPropertyId(MapUtils.getString(orderInfoMap, "PROPERTY_ID"));
        book002Tranrs.setPaymentId(MapUtils.getString(orderInfoMap, "PAYMENT_ID"));
        book002Tranrs.setHotelCharges(MapUtils.getInteger(orderInfoMap, "HOTEL_CHARGES"));
        book002Tranrs.setCheckIn(MapUtils.getString(orderInfoMap, "CHECK_IN"));
        book002Tranrs.setCheckOut(MapUtils.getString(orderInfoMap, "CHECK_OUT"));
        book002Tranrs.setStatus(MapUtils.getString(orderInfoMap, "STATUS"));
        book002Tranrs.setNote(MapUtils.getString(orderInfoMap, "NOTE"));

        if (!"y".equals(MapUtils.getString(orderInfoMap, "GUEST"))) {
            book002Tranrs.setGuestName(MapUtils.getString(orderInfoMap, "GUEST_NAME"));
            book002Tranrs.setGuestPhone(MapUtils.getString(orderInfoMap, "GUEST_PHONE"));
        } else {
            book002Tranrs.setGuestName(MapUtils.getString(orderInfoMap, "NAME"));
            book002Tranrs.setGuestPhone(MapUtils.getString(orderInfoMap, "PHONE"));
        }

        if (MapUtils.getObject(orderInfoMap, "CREATED_AT") instanceof Timestamp ts) {
            book002Tranrs.setCreatedAt(ts.toLocalDateTime());
        } else {
            book002Tranrs.setCreatedAt(null);
        }

        if (MapUtils.getObject(orderInfoMap, "UPDATED_AT") instanceof Timestamp ts) {
            book002Tranrs.setUpdatedAt(ts.toLocalDateTime());
        } else {
            book002Tranrs.setUpdatedAt(null);
        }

        book002Tranrs.setOrderDetail(orderDetails);

        log.info("[BOOK-002] 取得該筆訂單成功");
        return new Res<BOOK002Tranrs>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), book002Tranrs);
    }
}