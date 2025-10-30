package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.MediaBase64Entity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.repository.MediaBase64Repository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.USER006Svc;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class USER006SvcImpl implements USER006Svc {

    /** sqlAction */
    private final SqlAction sqlAction;
    /** sqlUtils */
    private final SqlUtils sqlUtils;
    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** MediaBase64Repository */
    private final MediaBase64Repository mediaBase64Repo;
    /** ObjectMapper */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 取得歷史訂單紀錄列表
     * @param accountId
     * @param req
     * @return
     * @throws IOException
     * @throws DataNotFoundException
     */
    @Override
    public Res<USER006Tranrs> getBookingList(String accountId, Req<USER006Tranrq> req)
            throws IOException, DataNotFoundException {
        log.info("[USER-006] 取得訂單歷史紀錄");
        String userId = usersRepo.findIdByAccountId(accountId);

        USER006Tranrq tranrq = (req != null && req.getTranrq() != null)
                ? req.getTranrq() : new USER006Tranrq();

        String sortMode = StringUtils.isNotBlank(tranrq.getSortMode())
                ? tranrq.getSortMode().trim().toUpperCase()
                : "CREATED_DESC";
        switch (sortMode) {
            case "CHECKIN_ASC":
            case "CHECKIN_DESC":
            case "CREATED_ASC":
            case "CREATED_DESC":
                break;
            default:
                sortMode = "CREATED_DESC";
        }

        int page = tranrq.getPage() != null && tranrq.getPage() > 0 ? tranrq.getPage() : 1;
        int pageSize = tranrq.getPageSize() != null && tranrq.getPageSize() > 0 ? tranrq.getPageSize() : 10;
        int offset = (page - 1) * pageSize;

        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        if (StringUtils.isNotBlank(tranrq.getStatus())) param.put("status", tranrq.getStatus());
        if (StringUtils.isNotBlank(tranrq.getFrom())) param.put("from", tranrq.getFrom());
        if (StringUtils.isNotBlank(tranrq.getTo())) param.put("to", tranrq.getTo());
        param.put("sortMode", sortMode);
        param.put("offset", offset);
        param.put("limit", pageSize);

        log.info("[USER-006] 查詢參數: {}", param);

        String sql = sqlUtils.getDynamicQuerySQL("USER006_QUERY.sql", param);
        List<Map<String, Object>> mapList = sqlAction.queryForList(sql, param);

        if (mapList == null || mapList.isEmpty()) {
            throw new DataNotFoundException("查無歷史訂單資料");
        }

        List<String> mediaIds = mapList.stream()
                .map(m -> (String) m.get("mediaId"))
                .filter(id -> id != null && !id.isBlank())
                .distinct()
                .toList();

        Map<String, MediaBase64Entity> mediaMap = mediaIds.isEmpty()
                ? Map.of()
                : mediaBase64Repo.findAllById(mediaIds).stream()
                .collect(Collectors.toMap(MediaBase64Entity::getId, x -> x));

        for (Map<String, Object> r : mapList) {
            String mid = (String) r.get("mediaId");
            MediaBase64Entity m = mediaMap.get(mid);
            if (m != null && m.getBase64Data() != null) {
                String mime = (m.getMimeType() == null || m.getMimeType().isBlank())
                        ? "image/png" : m.getMimeType();
                r.put("imageUrl", "data:" + mime + ";base64," + m.getBase64Data());
            } else {
                r.put("imageUrl", null);
            }
            r.remove("mediaId");
        }

        List<USER006TranrsOrders> orders = mapList.stream()
                .map(m -> objectMapper.convertValue(m, USER006TranrsOrders.class))
                .toList();

        Map<String, Object> paramCount = new HashMap<>();
        paramCount.put("userId", userId);
        if (StringUtils.isNotBlank(tranrq.getStatus())) paramCount.put("status", tranrq.getStatus());
        if (StringUtils.isNotBlank(tranrq.getFrom())) paramCount.put("from", tranrq.getFrom());
        if (StringUtils.isNotBlank(tranrq.getTo())) paramCount.put("to", tranrq.getTo());

        String countSql = sqlUtils.getDynamicQuerySQL("USER006_COUNT.sql", paramCount);
        List<Map<String, Object>> countRows = sqlAction.queryForList(countSql, paramCount);
        int totalCount = (countRows != null && !countRows.isEmpty())
                ? ((Number) countRows.get(0).get("TOTAL_COUNT")).intValue()
                : 0;
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        USER006Tranrs tranrs = new USER006Tranrs(orders, totalCount, totalPages, page);
        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
