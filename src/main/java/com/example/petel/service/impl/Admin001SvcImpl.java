package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.Admin001Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Admin001SvcImpl implements Admin001Svc {

    private final SqlUtils sqlUtils;
    private final SqlAction sqlAction;

    /**
     * 查詢所有旅館列表
     * @param req Req<Admin001Tranrq>
     * @return Res<Admin001Tranrs>
     */
    @Override
    public Res<Admin001Tranrs> queryStores(Req<Admin001Tranrq> req) throws DataNotFoundException, IOException {
        log.info("-------- [ADMIN-001] 查詢所有旅館列表 ---------");
        Admin001Tranrq tranrq = req.getTranrq();

        // 建立查詢參數 Map
        Map<String, Object> paramMap = new HashMap<>();

        // 動態組裝 SQL 參數 (所有欄位都支援模糊查詢)
        if (org.apache.commons.lang3.StringUtils.isNotBlank(tranrq.getPropertyName())) {
            paramMap.put("propertyName", "%" + tranrq.getPropertyName() + "%");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(tranrq.getTel())) {
            paramMap.put("tel", "%" + tranrq.getTel() + "%");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(tranrq.getPostalCode())) {
            paramMap.put("postalCode", "%" + tranrq.getPostalCode() + "%");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(tranrq.getAddress())) {
            paramMap.put("address", "%" + tranrq.getAddress() + "%");
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(tranrq.getSellerName())) {
            paramMap.put("sellerName", "%" + tranrq.getSellerName() + "%");
        }

        // 使用獨立的 COUNT SQL 查詢總筆數 (不包含分頁參數)
        String countSql = sqlUtils.getDynamicQuerySQL("ADMIN001_COUNT.sql", paramMap);
        log.info("[ADMIN-001] 執行 COUNT SQL: {}", countSql);

        List<Map<String, Object>> countResult = sqlAction.queryForList(countSql, paramMap);
        Integer totalCount = countResult.isEmpty() ? 0 :
            ((Number) countResult.get(0).get("TOTAL_COUNT")).intValue();

        // 檢查是否有資料
        if (totalCount == null || totalCount == 0) {
            log.warn("[ADMIN-001] 查無資料");
            throw new DataNotFoundException("查無旅館資料");
        }

        // 計算分頁參數
        Admin001TranrqPage page = tranrq.getPage();
        int pageNumber = (page != null && page.getPageNumber() != null) ? page.getPageNumber() : 1;
        int pageSize = (page != null && page.getPageSize() != null) ? page.getPageSize() : 10;
        int offset = (pageNumber - 1) * pageSize;

        // 加入分頁參數
        paramMap.put("offset", offset);
        paramMap.put("pageSize", pageSize);

        // 計算總頁數
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 取得動態 SQL (已包含分頁)
        String sql = sqlUtils.getDynamicQuerySQL("ADMIN001_QUERY.sql", paramMap);
        log.info("[ADMIN-001] 執行 SQL: {}", sql);

        // 執行查詢 (使用包含分頁的 SQL)
        List<Admin001TranrsTranrs> hotels = sqlAction.queryForListVO(
                sql,
                paramMap,
                Admin001TranrsTranrs.class,
                true
        );

        log.info("[ADMIN-001] 查詢成功，共 {} 筆，當前頁 {}/{}", totalCount, pageNumber, totalPages);

        // 組裝回應
        Admin001Tranrs tranrs = new Admin001Tranrs();
        tranrs.setHotels(hotels != null ? hotels : new ArrayList<>());
        tranrs.setTotalCount(totalCount != null ? totalCount : 0);
        tranrs.setTotalPages(totalPages);
        tranrs.setCurrentPage(pageNumber);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
