package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.LicenseRepository;
import com.example.petel.repository.PropertyRepository;
import com.example.petel.service.MERCH008Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH008SvcImpl implements MERCH008Svc {

    private final PropertyRepository propertyRepository;
    private final LicenseRepository licenseRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH008Tranrs> insert(Req<MERCH008Tranrq> requestBody)
            throws InsertFailException, DataNotFoundException {

        log.info("-------- [MERCH-008] 新增旅館資訊 ---------");
        MERCH008Tranrq rq = requestBody.getTranrq();

        var licenseOpt = licenseRepository.findByNameAndBusinessCode(rq.getName(), rq.getBusinessCode());

        if (licenseOpt.isEmpty()) {
            log.warn("[MERCH-008] 驗證失敗：查無此業者或特寵編號不符");
            throw new DataNotFoundException("查無此業者或特寵業編號不符，無法新增旅館。");
        }

        String newPropertyId = IdUtil.generateTableId("P", propertyRepository.findMaxId());
        log.info("[MERCH-008] 生成旅館ID: {}", newPropertyId);

        try {
            PropertyEntity property = new PropertyEntity();
            property.setId(newPropertyId);
            property.setSellerId(rq.getSellerId());
            property.setName(rq.getName());
            property.setBusinessCode(rq.getBusinessCode());
            property.setTel(rq.getTel());
            property.setPostalCode(rq.getPostalCode());
            property.setAddress(rq.getAddress());
            property.setBankAccount(rq.getBankAccount());
            property.setInfo(rq.getInfo());
            property.setCheckNotice(rq.getCheckNotice());
            property.setPetNotice(rq.getPetNotice());
            property.setPropertyNotice(rq.getPropertyNotice());

            propertyRepository.save(property);

        } catch (Exception e) {
            log.error("[MERCH-008] 新增旅館失敗", e);
            throw new InsertFailException("新增旅館失敗：" + e.getMessage());
        }

        MERCH008Tranrs rs = new MERCH008Tranrs();
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), rs);
    }
}