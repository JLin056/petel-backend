package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.entity.PropertyFacilitiesEntity;
import com.example.petel.entity.PropertyImageEntity;
import com.example.petel.entity.RoomImageEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.*;
import com.example.petel.service.MERCH007Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH007SvcImpl implements MERCH007Svc {

    private final PropertyRepository propertyRepository;
    private final PostalsRepository postalsRepository;
    private final PropertyFacilitiesRepository propertyFacilitiesRepository;
    private final PropertyImageRepository propertyImageRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH007Tranrs> update(Req<MERCH007Tranrq> requestBody) throws DataNotFoundException, UpdateFailException {
        log.info("-------- [MERCH-007] 旅館資訊修改 ---------");

        MERCH007Tranrq tranrq = requestBody.getTranrq();
        String propertyId = tranrq.getId();

        Optional<PropertyEntity> optional = propertyRepository.findById(propertyId);
        if (optional.isEmpty()) {
            log.warn("[MERCH-007] 查無資料 propertyId={}", propertyId);
            throw new DataNotFoundException("查無旅館資料");
        }

        PropertyEntity entity = optional.get();

        try {
            if (tranrq.getTel() != null) {
                entity.setTel(tranrq.getTel());
            }

            if (tranrq.getCity() != null && tranrq.getDistrict() != null && tranrq.getAddressDetail() != null) {
                var postalOpt = postalsRepository.findByCityAndDistrict(tranrq.getCity(), tranrq.getDistrict());
                if (postalOpt.isEmpty()) {
                    log.warn("[MERCH-007] 查無此縣市區域組合: {} {}", tranrq.getCity(), tranrq.getDistrict());
                    throw new DataNotFoundException("查無此縣市區域組合，無法更新旅館。");
                }
                String postalId = postalOpt.get().getId();
                String fullAddress = tranrq.getCity() + tranrq.getDistrict() + tranrq.getAddressDetail();
                entity.setPostalCode(postalId);
                entity.setAddress(fullAddress);
                log.info("[MERCH-007] 更新地址資訊 postalId={}, address={}", postalId, fullAddress);
            }

            if (tranrq.getBankAccount() != null) entity.setBankAccount(tranrq.getBankAccount());
            if (tranrq.getInfo() != null) entity.setInfo(tranrq.getInfo());
            if (tranrq.getCheckNotice() != null) entity.setCheckNotice(tranrq.getCheckNotice());
            if (tranrq.getPetNotice() != null) entity.setPetNotice(tranrq.getPetNotice());
            if (tranrq.getPropertyNotice() != null) entity.setPropertyNotice(tranrq.getPropertyNotice());

            if (tranrq.getFacilities() != null) {
                propertyFacilitiesRepository.deleteByPropertyId(propertyId);
                log.info("[MERCH-007] 已刪除舊的設備關聯");

                if (!tranrq.getFacilities().isEmpty()) {
                    for (String facilityId : tranrq.getFacilities()) {
                        String newId = IdUtil.generateTableId("G", propertyFacilitiesRepository.findMaxId());
                        PropertyFacilitiesEntity pf = new PropertyFacilitiesEntity();
                        pf.setId(newId);
                        pf.setPropertyId(propertyId);
                        pf.setFacilityId(facilityId);
                        propertyFacilitiesRepository.save(pf);
                    }
                    log.info("[MERCH-007] 已新增 {} 個設備關聯", tranrq.getFacilities().size());
                } else {
                    log.info("[MERCH-007] 清空所有設備關聯");
                }
            }

            if (tranrq.getPropertyImages() != null && !tranrq.getPropertyImages().isEmpty()) {
                for (MERCH007TranrqPropertyImage propertyImage : tranrq.getPropertyImages()) {
                    PropertyImageEntity propertyImageEntity = new PropertyImageEntity();
                    propertyImageEntity.setPropertyId(propertyId);
                    propertyImageEntity.setMediaId(propertyImage.getMediaId());
                    propertyImageEntity.setSortOrder(propertyImage.getSortOrder());

                    propertyImageRepository.save(propertyImageEntity);
                    log.info("[MERCH-007] 新增旅館圖片關聯：propertyId={}, mediaId={}, sortOrder={}",
                            propertyId, propertyImage.getMediaId(), propertyImage.getSortOrder());
                }
                log.info("[MERCH-007] 共新增 {} 張旅館圖片", tranrq.getPropertyImages().size());
            } else {
                log.info("[MERCH-007] 未提供旅館圖片");
            }

            propertyRepository.save(entity);
            log.info("[MERCH-007] 更新成功 propertyId={}", propertyId);

        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("[MERCH-007] 更新旅館資訊失敗", e);
            throw new UpdateFailException("更新旅館資訊失敗：" + e.getMessage());
        }

        MERCH007Tranrs resData = new MERCH007Tranrs();
        resData.setId(entity.getId());
        resData.setName(entity.getName());
        resData.setBusinessCode(entity.getBusinessCode());
        resData.setTel(entity.getTel());
        resData.setAddress(entity.getAddress());
        resData.setBankAccount(entity.getBankAccount());
        resData.setInfo(entity.getInfo());
        resData.setCheckNotice(entity.getCheckNotice());
        resData.setPetNotice(entity.getPetNotice());
        resData.setPropertyNotice(entity.getPropertyNotice());

        var facilityList = propertyFacilitiesRepository.findByPropertyId(propertyId)
                .stream()
                .map(PropertyFacilitiesEntity::getFacilityId)
                .toList();
        resData.setFacilities(facilityList);

        log.info("[MERCH-007] 回傳設施數量={}", facilityList.size());

        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), resData);
    }
}