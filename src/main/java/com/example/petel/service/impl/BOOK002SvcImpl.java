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
    private static final String sqlFile = "BOOK_QUERY.sql";
    /** sqlAction */
    private final SqlAction sqlAction;
    /** sqlUtils */
    private final SqlUtils sqlUtils;

    /**
     * 取得該筆訂單
     *
     * @param requestBody Req<BOOK002Tranrq>
     * @return Res<BOOKTranrs>
     * @throws DataNotFoundException 查無資料
     * @throws IOException           檔案讀寫異常
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<BOOKTranrs> book002(Req<BOOK002Tranrq> requestBody) throws DataNotFoundException, IOException {

        log.info("-------- [BOOK-002] 取得該筆訂單 API 啟動 --------");

        Long orderId = requestBody.getTranrq().getOrderId();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderID", orderId);
        String sql = sqlUtils.getQuerySql(sqlFile);
        List<Map<String, Object>> mapList = sqlAction.queryForList(sql, paramMap);

        if (mapList.isEmpty()) {
            log.error("[BOOK-002] 查無資料，訂單查詢失敗");
            throw new DataNotFoundException();
        }

        List<BOOKTranrqOrderDetail> orderDetails = new ArrayList<>();

        for (Map<String, Object> queryResult : mapList) {
            BOOKTranrqOrderDetail orderDetail = new BOOKTranrqOrderDetail();
            orderDetail.setRoomId(MapUtils.getLong(queryResult, "ROOM_ID"));
            orderDetail.setArrivalDate(MapUtils.getString(queryResult, "ARRIVAL_DATE"));
            orderDetail.setRoomQuantity(MapUtils.getInteger(queryResult, "QUANTITY"));
            orderDetail.setRoomPrice(MapUtils.getInteger(queryResult, "PRICE"));
            orderDetails.add(orderDetail);
        }

        Map<String, Object> orderInfoMap = mapList.get(0);

        BOOKTranrqOrderInfo orderInfo = new BOOKTranrqOrderInfo();
        orderInfo.setUserId(MapUtils.getLong(orderInfoMap, "USER_ID"));
        orderInfo.setPropertyId(MapUtils.getLong(orderInfoMap, "PROPERTY_ID"));
        orderInfo.setPaymentId(MapUtils.getInteger(orderInfoMap, "PAYMENT_ID"));
        orderInfo.setHotelCharges(MapUtils.getInteger(orderInfoMap, "HOTEL_CHARGES"));
        orderInfo.setCheckIn(MapUtils.getString(orderInfoMap, "CHECK_IN"));
        orderInfo.setCheckOut(MapUtils.getString(orderInfoMap, "CHECK_OUT"));
        orderInfo.setStatus(MapUtils.getString(orderInfoMap, "STATUS"));
        orderInfo.setNote(MapUtils.getString(orderInfoMap, "NOTE"));

        log.info("[BOOK-002] 取得該筆訂單成功");
        return new Res<BOOKTranrs>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), new BOOKTranrs(orderId, orderInfo, orderDetails));
    }
}