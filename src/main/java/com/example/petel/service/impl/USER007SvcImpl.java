package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaBase64Entity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.repository.MediaBase64Repository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.USER007Svc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class USER007SvcImpl implements USER007Svc {

    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** SqlAction */
    private final SqlAction sqlAction;
    /** SqlUtils */
    private final SqlUtils sqlUtils;
    /** MediaBase64Repository */
    private final MediaBase64Repository mediaBase64Repo;
    /** ObjectMapper */
    private final ObjectMapper objectMapper;

    @Override
    public Res<USER007Tranrs> getOrderDetail(Req<USER007Tranrq> req)
            throws IOException, DataNotFoundException {
        log.info("---- [USER-007] 取得訂單詳細資訊 ----");
        String orderId = req.getTranrq().getOrderId();

        HashMap<String, Object> param = new HashMap<>();
        param.put("orderId", orderId);

        // 查 header
        log.info("[USER-007] 取得訂單資訊");
        String queryHeaderSql = sqlUtils.getDynamicQuerySQL("USER007_QUERY_HEADER.sql", param);
        List<Map<String, Object>> headerRows = sqlAction.queryForList(queryHeaderSql, param);
        if (headerRows == null || headerRows.isEmpty()) {
            throw new DataNotFoundException("查無訂單資料");
        }
        Map<String, Object> headerRow = headerRows.get(0);

        // 查 base64
        log.info("[USER-007] 取得圖片資訊");
        String propertyImageUrl = null;
        String propertyMediaId = (String) headerRow.get("propertyMediaId");
        if (propertyMediaId != null && !propertyMediaId.isBlank()) {
            MediaBase64Entity mb = mediaBase64Repo.findById(propertyMediaId).orElse(null);
            if (mb != null && mb.getBase64Data() != null) {
                String mime = (mb.getMimeType() == null || mb.getMimeType().isBlank()) ? "image/png" : mb.getMimeType();
                propertyImageUrl = "data:" + mime + ";base64," + mb.getBase64Data();
            }
        }
        headerRow.put("propertyImageUrl", propertyImageUrl);
        headerRow.remove("propertyMediaId");
        USER007TranrsHeader header = objectMapper.convertValue(headerRow, USER007TranrsHeader.class);

        // 查 items
        log.info("[USER-007] 取得訂單詳細資訊");
        String queryItemsSql = sqlUtils.getDynamicQuerySQL("USER007_QUERY_ITEMS.sql", param);
        List<Map<String, Object>> itemsRows = sqlAction.queryForList(queryItemsSql, param);
        if (itemsRows == null) itemsRows = List.of();

        List<USER007TranrsItems> items = itemsRows.stream()
                .map(m -> {
                    USER007TranrsItems item = objectMapper.convertValue(m, USER007TranrsItems.class);
                    return item;
                })
                .toList();

        // 查 tranrs
        log.info("[USER-007] 取得訂單詳細資訊總數");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkIn  = LocalDate.parse(header.getCheckIn(), fmt);
        LocalDate checkOut = LocalDate.parse(header.getCheckOut(), fmt);

        int nights = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
        int totalQty = items.stream().mapToInt(USER007TranrsItems::getQuantity).sum();
        Integer totalAmout = header.getHotelCharges();

        USER007Tranrs tranrs = new USER007Tranrs(header, items, nights, totalQty, totalAmout);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
