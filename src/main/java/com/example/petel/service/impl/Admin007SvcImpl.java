package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.Admin007Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class Admin007SvcImpl implements Admin007Svc {

    private final SqlUtils sqlUtils;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 查詢會員列表
     * @param req Req<Admin007Tranrq>
     * @return Res<Admin007Tranrs>
     */
    @Override
    public Res<Admin007Tranrs> queryMembers(Req<Admin007Tranrq> req) throws Exception {
        log.info("-------- [ADMIN-007] 查詢會員列表 ---------");
        Admin007Tranrq tranrq = req.getTranrq();

        // 建立查詢參數 Map
        Map<String, Object> paramMap = new HashMap<>();

        // 動態組裝 SQL 參數
        if (StringUtils.isNotBlank(tranrq.getAccountsId())) {
            paramMap.put("accountsId", tranrq.getAccountsId());
        }
        if (StringUtils.isNotBlank(tranrq.getEmail())) {
            paramMap.put("email", tranrq.getEmail());
        }
        if (StringUtils.isNotBlank(tranrq.getName())) {
            // 模糊查詢：加上 %
            paramMap.put("name", "%" + tranrq.getName() + "%");
        }
        if (StringUtils.isNotBlank(tranrq.getPhone())) {
            paramMap.put("phone", tranrq.getPhone());
        }

        // 取得動態 SQL
        String sql = sqlUtils.getDynamicQuerySQL("ADMIN007_QUERY.sql", paramMap);
        log.info("[ADMIN-007] 執行 SQL: {}", sql);

        // 查詢總筆數
        String countSql = "SELECT COUNT(*) FROM (" + sql + ") temp_table";
        Integer totalCount = namedParameterJdbcTemplate.queryForObject(
                countSql,
                new MapSqlParameterSource(paramMap),
                Integer.class
        );

        // 檢查是否有資料
        if (totalCount == null || totalCount == 0) {
            log.warn("[ADMIN-007] 查無資料");
            throw new com.example.petel.exception.DataNotFoundException("查無會員資料");
        }

        // 計算分頁
        Admin007TranrqPage page = tranrq.getPage();
        int pageNumber = (page != null && page.getPageNumber() != null) ? page.getPageNumber() : 1;
        int pageSize = (page != null && page.getPageSize() != null) ? page.getPageSize() : 10;
        int offset = (pageNumber - 1) * pageSize;
        int totalPages = (totalCount != null && totalCount > 0) ? (int) Math.ceil((double) totalCount / pageSize) : 0;

        // 加入排序和分頁查詢
        String pagedSql = sql + " ORDER BY a.ACCOUNTS_ID OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY";
        paramMap.put("offset", offset);
        paramMap.put("pageSize", pageSize);

        // 執行查詢
        List<Admin007TranrsTranrs> members = namedParameterJdbcTemplate.query(
                pagedSql,
                new MapSqlParameterSource(paramMap),
                (rs, rowNum) -> new Admin007TranrsTranrs(
                        rs.getInt("ACCOUNTS_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("USERS_NAME"),
                        rs.getString("USERS_PHONE"),
                        rs.getString("ROLE"),
                        rs.getString("STATUS")
                )
        );

        log.info("[ADMIN-007] 查詢成功，共 {} 筆，當前頁 {}/{}", totalCount, pageNumber, totalPages);

        // 組裝回應
        Admin007Tranrs tranrs = new Admin007Tranrs();
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
