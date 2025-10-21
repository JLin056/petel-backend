package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.ADMIN002Svc;
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
public class ADMIN002SvcImpl implements ADMIN002Svc {

    private final SqlUtils sqlUtils;
    private final SqlAction sqlAction;

    /**
     * 查詢賣家列表
     * @param req Req<ADMIN002Tranrq>
     * @return Res<ADMIN002Tranrs>
     */
    @Override
    public Res<ADMIN002Tranrs> querySellers(Req<ADMIN002Tranrq> req) throws DataNotFoundException, IOException {
        log.info("-------- [ADMIN-002] 查詢賣家列表 ---------");
        ADMIN002Tranrq tranrq = req.getTranrq();

        // 建立查詢參數 Map
        Map<String, Object> paramMap = new HashMap<>();

        // 動態組裝 SQL 參數
        if (StringUtils.isNotBlank(tranrq.getSellerId())) {
            paramMap.put("sellerId", tranrq.getSellerId());
        }
        if (StringUtils.isNotBlank(tranrq.getAccountId())) {
            paramMap.put("accountId", tranrq.getAccountId());
        }
        if (StringUtils.isNotBlank(tranrq.getEmail())) {
            paramMap.put("email", tranrq.getEmail());
        }
        if (StringUtils.isNotBlank(tranrq.getName())) {
            // 模糊查詢：加上 %
            paramMap.put("name", "%" + tranrq.getName() + "%");
        }
        if (StringUtils.isNotBlank(tranrq.getBusinessCode())) {
            paramMap.put("businessCode", tranrq.getBusinessCode());
        }

        // 查詢總筆數 (使用相同的條件參數)
        String countSql = sqlUtils.getDynamicQuerySQL("ADMIN002_Count.sql", paramMap);
        List<Map<String, Object>> countResult = sqlAction.queryForList(countSql, paramMap);
        Integer totalCount = 0;
        if (countResult != null && !countResult.isEmpty()) {
            Object countObj = countResult.get(0).get("TOTAL_COUNT");
            totalCount = countObj != null ? ((Number) countObj).intValue() : 0;
        }

        // 檢查是否有資料
        if (totalCount == null || totalCount == 0) {
            log.warn("[ADMIN-002] 查無資料");
            throw new com.example.petel.exception.DataNotFoundException("查無賣家資料");
        }

        // 計算分頁參數並加入 paramMap
        PageRequest page = tranrq.getPage();
        int pageNumber = (page != null && page.getPageNumber() != null) ? page.getPageNumber() : 1;
        int pageSize = (page != null && page.getPageSize() != null) ? page.getPageSize() : 5;
        int offset = (pageNumber - 1) * pageSize;

        paramMap.put("offset", offset);
        paramMap.put("pageSize", pageSize);

        // 計算總頁數
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 執行查詢 (使用包含分頁的 SQL)
        String sql = sqlUtils.getDynamicQuerySQL("ADMIN002_Query.sql", paramMap);
        log.info("[ADMIN-002] 執行 SQL: {}", sql);

        List<ADMIN002TranrsTranrs> sellers = sqlAction.queryForListVO(
                sql,
                paramMap,
                ADMIN002TranrsTranrs.class,
                true
        );

        log.info("[ADMIN-002] 查詢成功，共 {} 筆，當前頁 {}/{}", totalCount, pageNumber, totalPages);

        // 組裝回應
        ADMIN002Tranrs tranrs = new ADMIN002Tranrs();
        tranrs.setSellers(sellers != null ? sellers : new ArrayList<>());
        tranrs.setTotalCount(totalCount != null ? totalCount : 0);
        tranrs.setTotalPages(totalPages);
        tranrs.setCurrentPage(pageNumber);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
