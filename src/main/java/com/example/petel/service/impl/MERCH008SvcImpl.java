package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.entity.PropertyFacilitiesEntity;
import com.example.petel.entity.PropertyImageEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.LicenseRepository;
import com.example.petel.repository.PostalsRepository;
import com.example.petel.repository.PropertyFacilitiesRepository;
import com.example.petel.repository.PropertyImageRepository;
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
    private final PostalsRepository postalsRepository;
    private final PropertyFacilitiesRepository propertyFacilitiesRepository;
    private final PropertyImageRepository propertyImageRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH008Tranrs> insert(Req<MERCH008Tranrq> requestBody) throws InsertFailException, DataNotFoundException {
        log.info("-------- [MERCH-008] 新增旅館資訊 ---------");

        MERCH008Tranrq rq = requestBody.getTranrq();
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

        var postalOpt = postalsRepository.findByCityAndDistrict(rq.getCity(), rq.getDistrict());
        if (postalOpt.isEmpty()) {
            log.warn("[MERCH-008] 查無此縣市區域組合: {} {}", rq.getCity(), rq.getDistrict());
            throw new DataNotFoundException("查無此縣市區域組合，無法新增旅館。");
        }

        String postalId = postalOpt.get().getId();
        String fullAddress = rq.getCity() + rq.getDistrict() + rq.getAddressDetail();
        String newPropertyId = IdUtil.generateTableId("P", propertyRepository.findMaxId());

        try {
            PropertyEntity property = new PropertyEntity();
            property.setId(newPropertyId);
            property.setSellerId(rq.getSellerId());
            property.setName(rq.getName());
            property.setBusinessCode(inputCode);
            property.setTel(rq.getTel());
            property.setPostalCode(postalId);
            property.setAddress(fullAddress);
            property.setBankAccount(rq.getBankAccount());
            property.setInfo(rq.getInfo());
            property.setCheckNotice(rq.getCheckNotice());
            property.setPetNotice(rq.getPetNotice());
            property.setPropertyNotice(rq.getPropertyNotice());
            propertyRepository.save(property);
            log.info("[MERCH-008] 已新增旅館主檔 propertyId={}", newPropertyId);

            // === 建立旅館設施 ===
            if (rq.getFacilities() != null && !rq.getFacilities().isEmpty()) {
                String currentMaxId = propertyFacilitiesRepository.findMaxId();
                for (String facilityId : rq.getFacilities()) {
                    String newId = IdUtil.generateTableId("G", currentMaxId);
                    PropertyFacilitiesEntity pf = new PropertyFacilitiesEntity();
                    pf.setId(newId);
                    pf.setPropertyId(newPropertyId);
                    pf.setFacilityId(facilityId);
                    propertyFacilitiesRepository.save(pf);
                    currentMaxId = newId;  // 更新為當前ID，下次遞增
                }
                log.info("[MERCH-008] 已新增 {} 個設備關聯", rq.getFacilities().size());
            } else {
                log.info("[MERCH-008] 無設施資料");
            }

            // === 建立旅館圖片 ===
            if (rq.getPropertyImages() != null && !rq.getPropertyImages().isEmpty()) {
                for (MERCH008TranrqPropertyImage propertyImage : rq.getPropertyImages()) {
                    PropertyImageEntity propertyImageEntity = new PropertyImageEntity();
                    propertyImageEntity.setPropertyId(newPropertyId);
                    propertyImageEntity.setMediaId(propertyImage.getMediaId());
                    propertyImageEntity.setSortOrder(propertyImage.getSortOrder());
                    propertyImageRepository.save(propertyImageEntity);
                }
                log.info("[MERCH-008] 已新增 {} 張旅館圖片", rq.getPropertyImages().size());
            } else {
                log.info("[MERCH-008] 無圖片資料");
            }

        } catch (Exception e) {
            log.error("[MERCH-008] 新增旅館失敗", e);
            throw new InsertFailException("新增旅館失敗：" + e.getMessage());
        }

        // === 組回傳資料 ===
        MERCH008Tranrs rs = new MERCH008Tranrs();
        rs.setId(newPropertyId);
        rs.setSellerId(rq.getSellerId());
        rs.setName(rq.getName());
        rs.setBusinessCode(inputCode);
        rs.setTel(rq.getTel());
//        rs.setPostalCode(postalId);
        rs.setAddress(fullAddress);
        rs.setBankAccount(rq.getBankAccount());
        rs.setInfo(rq.getInfo());
        rs.setCheckNotice(rq.getCheckNotice());
        rs.setPetNotice(rq.getPetNotice());
        rs.setPropertyNotice(rq.getPropertyNotice());
        rs.setFacilities(rq.getFacilities());

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), rs);
    }
}
