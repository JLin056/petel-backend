package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.Admin001Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Admin001SvcImpl implements Admin001Svc {

    private final SqlUtils sqlUtils;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 查詢所有旅館列表
     * @param req Req<Admin001Tranrq>
     * @return Res<Admin001Tranrs>
     */
    @Override
    public Res<Admin001Tranrs> queryStores(Req<Admin001Tranrq> req) throws Exception {
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

        // 取得動態 SQL
        String sql = sqlUtils.getDynamicQuerySQL("ADMIN001_QUERY.sql", paramMap);
        log.info("[ADMIN-001] 執行 SQL: {}", sql);

        // 查詢總筆數
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") temp_table";
        Integer totalCount = namedParameterJdbcTemplate.queryForObject(
                countSql,
                new MapSqlParameterSource(paramMap),
                Integer.class
        );

        // 檢查是否有資料
        if (totalCount == null || totalCount == 0) {
            log.warn("[ADMIN-001] 查無資料");
            throw new DataNotFoundException("查無旅館資料");
        }

        // 計算分頁
        Admin001TranrqPage page = tranrq.getPage();
        int pageNumber = (page != null && page.getPageNumber() != null) ? page.getPageNumber() : 1;
        int pageSize = (page != null && page.getPageSize() != null) ? page.getPageSize() : 10;
        int offset = (pageNumber - 1) * pageSize;
        int totalPages = (totalCount != null && totalCount > 0) ? (int) Math.ceil((double) totalCount / pageSize) : 0;

        // 加入排序和分頁查詢
        String pagedSql = sql + " ORDER BY p.PROPERTY_ID OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY";
        paramMap.put("offset", offset);
        paramMap.put("pageSize", pageSize);

        // 執行查詢
        List<Admin001TranrsTranrs> hotels = namedParameterJdbcTemplate.query(
                pagedSql,
                new MapSqlParameterSource(paramMap),
                (rs, rowNum) -> new Admin001TranrsTranrs(
                        rs.getInt("PROPERTY_ID"),
                        rs.getString("PROPERTY_NAME"),
                        rs.getString("PROPERTY_TEL"),
                        rs.getString("PROPERTY_POSTAL_CODE"),
                        rs.getString("PROPERTY_ADDRESS"),
                        rs.getString("PROPERTY_BANK_ACCOUNT"),
                        rs.getString("SELLER_NAME"),
                        rs.getString("BUSINESS_CODE")
                )
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
