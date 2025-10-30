package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PostalRepository;
import com.example.petel.repository.PropertyRepository;
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

    /**
     * PropertyRepository
     */
    private final PropertyRepository propertyRepository;

    /**
     * PostalRepository
     */
    private final PostalRepository postalRepository;

    /**
     * 修改旅館資訊
     *
     * @param requestBody Req<MERCH007Tranrq> （propertyId)
     * @return Res<MERCH007Tranrs>
     * @throws DataNotFoundException,UpdateFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH007Tranrs> update(Req<MERCH007Tranrq> requestBody) throws DataNotFoundException, UpdateFailException {
        log.info("-------- [MERCH-007] 旅館資訊修改 ---------");

        MERCH007Tranrq dto = requestBody.getTranrq();
        String propertyId = dto.getId();

        Optional<PropertyEntity> optional = propertyRepository.findById(propertyId);
        if (optional.isEmpty()) {
            log.warn("[MERCH-007] 查無資料 propertyId={}", propertyId);
            throw new DataNotFoundException("查無旅館資料");
        }

        PropertyEntity entity = optional.get();

        try {
            if (dto.getTel() != null) entity.setTel(dto.getTel());

            if (dto.getCity() != null && dto.getDistrict() != null) {
                var postalOpt = postalRepository.findByCityAndDistrict(dto.getCity(), dto.getDistrict());
                if (postalOpt.isEmpty()) {
                    log.warn("[MERCH-007] 查無此縣市區域組合: {} {}", dto.getCity(), dto.getDistrict());
                    throw new DataNotFoundException("查無此縣市區域組合，無法更新旅館。");
                }
                String postalId = postalOpt.get().getId();
                String fullAddress = dto.getCity() + dto.getDistrict() +
                                   (dto.getAddressDetail() != null ? dto.getAddressDetail() : "");
                entity.setPostalCode(postalId);
                entity.setAddress(fullAddress);
                log.info("[MERCH-007] 更新地址資訊 postalId={}, address={}", postalId, fullAddress);
            }

            if (dto.getBankAccount() != null) entity.setBankAccount(dto.getBankAccount());
            if (dto.getInfo() != null) entity.setInfo(dto.getInfo());
            if (dto.getCheckNotice() != null) entity.setCheckNotice(dto.getCheckNotice());
            if (dto.getPetNotice() != null) entity.setPetNotice(dto.getPetNotice());
            if (dto.getPropertyNotice() != null) entity.setPropertyNotice(dto.getPropertyNotice());

            propertyRepository.save(entity);
            log.info("[MERCH-007] 更新成功 propertyId={}", propertyId);

        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("[MERCH-007] 更新旅館資訊失敗", e);
            throw new UpdateFailException("更新旅館資訊失敗");
        }

        MERCH007Tranrs resData = new MERCH007Tranrs();
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), resData);
    }
}