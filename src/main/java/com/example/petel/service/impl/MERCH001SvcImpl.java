package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.ErrorInputException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.MERCH001Svc;
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
public class MERCH001SvcImpl implements MERCH001Svc {

    /**
     * MERCH001_QUERY
     */
    private static final String MERCH001_QUERY = "MERCH001_QUERY.sql";
    /**
     * MERCH001_QUERY
     */
    private static final String MERCH001_TODAY_QUERY = "MERCH001_TODAY_QUERY.sql";
    /**
     * SqlAction
     */
    private final SqlAction sqlAction;
    /**
     * SqlUtilis
     */
    private final SqlUtils sqlUtils;

    /**
     * 訂單列表
     *
     * @param merch001Tranrq Req<MERCH001Tranrq> （propertyId)
     * @return Res<MERCH001Tranrs>
     * @throws DataNotFoundException, IOException
     */
    @Override
    public Res<MERCH001Tranrs<MERCH001TranrsBooking>> list(Req<MERCH001Tranrq> merch001Tranrq) throws DataNotFoundException, IOException {
        log.info("-------- [MERCH-001] 訂單列表 ---------");

        Map<String, Object> paramMap = new HashMap<>();
        String propertyId = merch001Tranrq.getTranrq().getPropertyId();
        log.info("[MERCH-001] 查詢 propertyId = {}", propertyId);
        paramMap.put("propertyId", propertyId);

        String arrivalDate = merch001Tranrq.getTranrq().getArrivalDate();
        if (arrivalDate != null && !arrivalDate.isBlank()) {
            log.info("[MERCH-001] 查詢 propertyId = {}, arrivalDate = {}", propertyId, arrivalDate);
            paramMap.put("arrivalDate", arrivalDate);
        } else {
            log.info("[MERCH-001] 查詢 propertyId = {}，查全部訂單", propertyId);
        }

        MERCH001TranrqPage pageData = merch001Tranrq.getTranrq().getPage();
        log.info("[MERCH-001] 查詢 pageData = {}", pageData);
        Integer pageSize = pageData.getPageSize();
        Integer pageNumber = pageData.getPageNumber();
        if (pageSize == null || pageSize <= 0) {
            log.warn("[MERCH-001] 分頁筆數錯誤");
            throw new ErrorInputException("分頁筆數錯誤:pageSize必須有值且>0");
        }
        if (pageNumber == null || pageNumber < 1) {
            log.warn("[MERCH-001] 頁碼錯誤");
            throw new ErrorInputException("頁碼錯誤:pageNumber必須有值且>1");
        }
        paramMap.put("pageSize", pageSize);
        paramMap.put("offset", (pageNumber - 1) * pageSize);

        String sqlFile = (arrivalDate != null && !arrivalDate.isBlank())
                ? MERCH001_TODAY_QUERY
                : MERCH001_QUERY;

        String sql = sqlUtils.getQuerySql(sqlFile);
        List<Map<String, Object>> mapList = sqlAction.queryForList(sql, paramMap);
        if (CollectionUtils.isEmpty(mapList)) {
            log.warn("[MERCH-001] 依據 propertyId 查無資料");
            throw new DataNotFoundException("查無訂單資料");
        }

        List<MERCH001TranrsBooking> bookingList = new ArrayList<>();
        mapList.forEach(map -> {
            MERCH001TranrsBooking booking = new MERCH001TranrsBooking();
            booking.setId(MapUtils.getString(map, "ID"));
            Object createdAtObj = MapUtils.getObject(map, "CREATED_AT");
            if (createdAtObj instanceof java.sql.Timestamp timestamp) {
                booking.setCreatedAt(timestamp.toLocalDateTime());
            } else if (createdAtObj instanceof java.time.LocalDateTime localDateTime) {
                booking.setCreatedAt(localDateTime);
            }
            booking.setUserName(MapUtils.getString(map, "USER_NAME"));
            booking.setCheckIn(MapUtils.getString(map, "CHECK_IN"));
            booking.setCheckOut(MapUtils.getString(map, "CHECK_OUT"));
            booking.setPaymentName(MapUtils.getString(map, "PAYMENT_NAME"));
            booking.setStatus(MapUtils.getString(map, "STATUS"));
            booking.setHotelCharges(MapUtils.getInteger(map, "HOTEL_CHARGES"));
            bookingList.add(booking);
        });
        log.info("[MERCH-001] 查詢成功，propertyId={}, bookingList={}", propertyId, bookingList);

        int totalCount = MapUtils.getInteger(mapList.get(0), "TOTAL_COUNT");

        MERCH001Tranrs<MERCH001TranrsBooking> merch001Tranrs = new MERCH001Tranrs<>();
        merch001Tranrs.setBookings(bookingList);
        merch001Tranrs.setPageNumber(pageData.getPageNumber());
        int totalPage = (int) Math.ceil(totalCount / pageData.getPageSize().doubleValue());
        merch001Tranrs.setTotalPage(totalPage);
        merch001Tranrs.setTotalCount(totalCount);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch001Tranrs
        );
    }
}
