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

        // 處理特寵編號格式
        var inputCode = rq.getBusinessCode().trim();
        if (!inputCode.startsWith("特寵業")) {
            inputCode = "特寵業繁字第" + inputCode + "號";
        }

        var licenseOpt = licenseRepository.findByNameAndBusinessCode(rq.getName(), inputCode);
        if (licenseOpt.isEmpty()) {
            log.warn("[MERCH-008] 驗證失敗：查無此業者或特寵編號不符");
            throw new DataNotFoundException("查無此業者或特寵業編號不符，無法新增旅館。");
        }

        var existingProperty = propertyRepository.findByNameOrBusinessCode(rq.getName(), inputCode);
        if (existingProperty.isPresent()) {
            log.warn("[MERCH-008] 重複新增：旅館名稱或特寵編號已存在");
            throw new InsertFailException("此旅館或特寵業編號已存在，無法重複新增。");
        }

        String newPropertyId = IdUtil.generateTableId("P", propertyRepository.findMaxId());
        log.info("[MERCH-008] 生成旅館ID: {}", newPropertyId);

        try {
            PropertyEntity property = new PropertyEntity();
            property.setId(newPropertyId);
            property.setSellerId(rq.getSellerId());
            property.setName(rq.getName());
            property.setBusinessCode(inputCode);
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
        rs.setId(newPropertyId);
        rs.setSellerId(rq.getSellerId());
        rs.setName(rq.getName());
        rs.setBusinessCode(rq.getBusinessCode());
        rs.setTel(rq.getTel());
        rs.setPostalCode(rq.getPostalCode());
        rs.setAddress(rq.getAddress());
        rs.setBankAccount(rq.getBankAccount());
        rs.setInfo(rq.getInfo());
        rs.setCheckNotice(rq.getCheckNotice());
        rs.setPetNotice(rq.getPetNotice());
        rs.setPropertyNotice(rq.getPropertyNotice());
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), rs);
    }
}