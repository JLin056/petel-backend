package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.BOOK002Svc;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BOOK002SvcImpl implements BOOK002Svc {

    private static final String sqlFile = "BOOK_QUERY.sql";

    private final SqlAction sqlAction;

    private final SqlUtils sqlUtils;

    @Override
    public Res<BOOKTranrs> book002(Req<BOOK002Tranrq> requestBody) throws Exception {

        Long orderId = requestBody.getTranrq().getOrderId();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderID", orderId);
        String sql = sqlUtils.getQuerySql(sqlFile);
        List<Map<String, Object>> mapList = sqlAction.queryForList(sql, paramMap);

        if (mapList.isEmpty()) {
            throw new DataNotFoundException();
        }

        List<BOOKTranrqOrderDetail> orderDetails = new ArrayList<>();

        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map = mapList.get(i);
            BOOKTranrqOrderDetail orderDetail = new BOOKTranrqOrderDetail();
            orderDetail.setProductId(MapUtils.getLong(map, "PRODUCT_ID"));
            orderDetail.setArrivalDate(MapUtils.getString(map, "ARRIVAL_DATE"));
            orderDetail.setProductQuantity(MapUtils.getInteger(map, "QUANTITY"));
            orderDetail.setProductPrice(MapUtils.getDouble(map, "PRICE"));
            orderDetails.add(orderDetail);
        }

        Map<String, Object> zeroIndexMap = mapList.get(0);

        BOOKTranrqOrderInfo orderInfo = new BOOKTranrqOrderInfo();
        orderInfo.setUserId(MapUtils.getLong(zeroIndexMap, "USER_ID"));
        orderInfo.setPropertyId(MapUtils.getLong(zeroIndexMap, "PROPERTY_ID"));
        orderInfo.setPaymentId(MapUtils.getInteger(zeroIndexMap, "PAYMENT_ID"));
        orderInfo.setHotelCharges(MapUtils.getDouble(zeroIndexMap, "HOTEL_CHARGES"));
        orderInfo.setCheckIn(MapUtils.getString(zeroIndexMap, "CHECK_IN"));
        orderInfo.setCheckOut(MapUtils.getString(zeroIndexMap, "CHECK_OUT"));
        orderInfo.setStatus(MapUtils.getString(zeroIndexMap, "STATUS"));
        orderInfo.setNote(MapUtils.getString(zeroIndexMap, "NOTE"));

        Res<BOOKTranrs> responseBody = new Res<>();
        responseBody.setMwHeader(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS));
        responseBody.setTranrs(new BOOKTranrs(orderId, orderInfo, orderDetails));
        return responseBody;
    }
}