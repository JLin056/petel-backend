package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.ADMIN007Svc;
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
public class ADMIN007SvcImpl implements ADMIN007Svc {

    private final SqlUtils sqlUtils;
    private final SqlAction sqlAction;

    /**
     * 查詢會員列表
     * @param req Req<ADMIN007Tranrq>
     * @return Res<ADMIN007Tranrs>
     * @throws DataNotFoundException 查無資料
     */
    @Override
    public Res<ADMIN007Tranrs> queryMembers(Req<ADMIN007Tranrq> req) throws DataNotFoundException, IOException {
        log.info("-------- [ADMIN-007] 查詢會員列表 ---------");
        ADMIN007Tranrq tranrq = req.getTranrq();

        // 建立查詢參數 Map
        Map<String, Object> paramMap = new HashMap<>();

        // 動態組裝 SQL 參數
        if (StringUtils.isNotBlank(tranrq.getAccountId())) {
            paramMap.put("accountId", "%" +tranrq.getAccountId()+ "%");
        }
        if (StringUtils.isNotBlank(tranrq.getEmail())) {
            paramMap.put("email", "%" +tranrq.getEmail()+ "%");
        }
        if (StringUtils.isNotBlank(tranrq.getName())) {
            // 模糊查詢：加上 %
            paramMap.put("name", "%" + tranrq.getName() + "%");
        }
        if (StringUtils.isNotBlank(tranrq.getPhone())) {
            paramMap.put("phone", "%" +tranrq.getPhone()+ "%");
        }

        // 計算分頁參數
        PageRequest page = tranrq.getPage();
        int pageNumber = (page != null && page.getPageNumber() != null) ? page.getPageNumber() : 1;
        int pageSize = (page != null && page.getPageSize() != null) ? page.getPageSize() : 5;
        int offset = (pageNumber - 1) * pageSize;

        // 加入分頁參數
        paramMap.put("offset", offset);
        paramMap.put("pageSize", pageSize);

        System.out.println("paramMap: " + paramMap);

        // 取得動態 SQL (已包含分頁)
        String sql = sqlUtils.getDynamicQuerySQL("Admin007_Query.sql", paramMap);
        System.out.println("處理後的 SQL: " + sql);
        log.info("[ADMIN-007] 執行 SQL: {}", sql);

        // 查詢總筆數 (使用獨立的 COUNT SQL - 不需要分頁參數)
        Map<String, Object> countParamMap = new HashMap<>();
        if (StringUtils.isNotBlank(tranrq.getAccountId())) {
            countParamMap.put("accountId","%" + tranrq.getAccountId()+ "%");
        }
        if (StringUtils.isNotBlank(tranrq.getEmail())) {
            countParamMap.put("email", "%" +tranrq.getEmail()+ "%");
        }
        if (StringUtils.isNotBlank(tranrq.getName())) {
            countParamMap.put("name", "%" + tranrq.getName() + "%");
        }
        if (StringUtils.isNotBlank(tranrq.getPhone())) {
            countParamMap.put("phone","%" + tranrq.getPhone()+ "%");
        }
        String countSql = sqlUtils.getDynamicQuerySQL("Admin007_Count.sql", countParamMap);
        List<Map<String, Object>> countResult = sqlAction.queryForList(countSql, countParamMap);
        Integer totalCount = 0;
        if (countResult != null && !countResult.isEmpty()) {
            Object countObj = countResult.get(0).get("TOTAL_COUNT");
            totalCount = countObj != null ? ((Number) countObj).intValue() : 0;
        }

        // 檢查是否有資料
        if (totalCount == null || totalCount == 0) {
            log.warn("[ADMIN-007] 查無資料");
            throw new com.example.petel.exception.DataNotFoundException("查無會員資料");
        }

        // 計算總頁數
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 執行查詢 (使用包含分頁的 SQL)
        System.out.println("執行查詢前的 paramMap: " + paramMap);
        List<ADMIN007TranrsTranrs> members = sqlAction.queryForListVO(
                sql,
                paramMap,
                ADMIN007TranrsTranrs.class,
                true
        );

        log.info("[ADMIN-007] 查詢成功，共 {} 筆，當前頁 {}/{}", totalCount, pageNumber, totalPages);
        System.out.println(members);

        // 組裝回應
        ADMIN007Tranrs tranrs = new ADMIN007Tranrs();
        tranrs.setMembers(members != null ? members : new ArrayList<>());
        tranrs.setTotalCount(totalCount != null ? totalCount : 0);
        tranrs.setTotalPages(totalPages);
        tranrs.setCurrentPage(pageNumber);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
