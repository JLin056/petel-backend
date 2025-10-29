package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PropertyRepository;
import com.example.petel.service.MERCH013Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH013SvcImpl implements MERCH013Svc {
    /**
     * PropertyRepository
     */
    private final PropertyRepository propertyRepository;


    @Override
    public Res<MERCH013Tranrs<MERCH013TranrsProperty>> getProperties(Req<MERCH013Tranrq> merch013Tranrq) throws DataNotFoundException {
        log.info("-------- [MERCH-013] 查詢單商家旅館 ---------");

        String sellerId = merch013Tranrq.getTranrq().getSellerId();
        log.info("[MERCH-013] 查詢 sellerId = {}", sellerId);

        List<PropertyEntity> properties = propertyRepository.findBySellerId(sellerId);
        if (properties.isEmpty()) {
            log.warn("[MERCH-013] 依據 sellerId 查無資料");
            throw new DataNotFoundException("查無旅館資料");
        }

        List<MERCH013TranrsProperty> propertyList = new ArrayList<>();
        for (PropertyEntity propertyEntity : properties) {
            MERCH013TranrsProperty property = new MERCH013TranrsProperty();
            property.setId(propertyEntity.getId());
            property.setName(propertyEntity.getName());
            property.setTel(propertyEntity.getTel());
            property.setAddress(propertyEntity.getAddress());
            propertyList.add(property);
        }

        log.info("[MERCH-013] 查詢成功，sellerId={}, propertyList={}", sellerId, propertyList);
        MERCH013Tranrs<MERCH013TranrsProperty> merch013Tranrs = new MERCH013Tranrs<>();
        merch013Tranrs.setProperties(propertyList);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), merch013Tranrs);
    }
}