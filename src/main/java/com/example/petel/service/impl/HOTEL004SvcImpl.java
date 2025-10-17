package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.sql.SqlAction;
import com.example.petel.model.sql.SqlUtils;
import com.example.petel.service.HOTEL004Svc;
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
public class HOTEL004SvcImpl implements HOTEL004Svc {

    /**
     * HOTEL004_QUERY
     */
    private static final String HOTEL004_QUERY = "HOTEL004_QUERY.sql";
    /**
     * SqlAction
     */
    private final SqlAction sqlAction;
    /**
     * SqlUtilis
     */
    private final SqlUtils sqlUtils;

    /**
     * 查詢單筆旅館設施
     *
     * @param hotel004Tranrq Req<HOTEL004Tranrq> （propertyId)
     * @return Res<HOTEL004Tranrs>
     * @throws DataNotFoundException, IOException
     */
    @Override
    public Res<HOTEL004Tranrs<HOTEL004TranrsFacility>> facilities(Req<HOTEL004Tranrq> hotel004Tranrq) throws DataNotFoundException, IOException {
        log.info("-------- [HOTEL-004] 查詢單旅館設施 ---------");
        Map<String, Object> paramMap = new HashMap<>();
        String propertyId = hotel004Tranrq.getTranrq().getPropertyId();
        log.info("[HOTEL-004] 查詢 propertyId = {}", propertyId);
        paramMap.put("PROPERTY_ID", propertyId);

        List<HOTEL004TranrsFacility> facilityList = new ArrayList<>();

        String sql = sqlUtils.getQuerySql(HOTEL004_QUERY);
        List<Map<String, Object>> mapList = sqlAction.queryForList(sql, paramMap);
        if (CollectionUtils.isEmpty(mapList)) {
            log.warn("[HOTEL-004] 依據 propertyId 查無資料");
            throw new DataNotFoundException("查無設施資料");
        }

        mapList.forEach(map -> {
            HOTEL004TranrsFacility facility = new HOTEL004TranrsFacility();
            facility.setName(MapUtils.getString(map, "NAME"));
            facilityList.add(facility);
        });
        log.info("[HOTEL-004] 查詢成功，propertyId={}, facilityList={}", propertyId, facilityList);


        HOTEL004Tranrs<HOTEL004TranrsFacility> hotel004Tranrs = new HOTEL004Tranrs<>();
        hotel004Tranrs.setFacilities(facilityList);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                hotel004Tranrs
        );
    }
}
