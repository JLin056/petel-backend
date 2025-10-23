package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.USER006Svc;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    /** s3Base */
    @Value("${petel.s3.public-base}")
    private String s3Base;

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

        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("s3Base", s3Base);

        if (StringUtils.isNotBlank(tranrq.getStatus())) param.put("status", tranrq.getStatus());
        if (StringUtils.isNotBlank(tranrq.getFrom())) param.put("from", tranrq.getFrom());
        if (StringUtils.isNotBlank(tranrq.getTo())) param.put("to", tranrq.getTo());

        int page = tranrq.getPage() != null && tranrq.getPage() > 0 ? tranrq.getPage() : 1;
        int pageSize = tranrq.getPageSize() != null && tranrq.getPageSize() > 0 ? tranrq.getPageSize() : 10;
        int offset = (page - 1) * pageSize;

        param.put("offset", offset);
        param.put("limit", pageSize);

        log.info("[USER-006] 查詢參數: {}", param);

        String sql = sqlUtils.getDynamicQuerySQL("USER006_QUERY.sql", param);
        List<USER006TranrsOrders> orders = sqlAction.queryForListVO(sql, param, USER006TranrsOrders.class, true);

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

        if (orders == null || orders.isEmpty()) {
            throw new DataNotFoundException("查無歷史訂單資料");
        }

        USER006Tranrs tranrs = new USER006Tranrs(orders, totalCount, totalPages, page);
        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
