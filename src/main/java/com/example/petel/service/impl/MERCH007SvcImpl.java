package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
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

        MERCH007Tranrq merch007Tranrq = requestBody.getTranrq();
        String propertyId = merch007Tranrq.getId();
        log.info("[MERCH-007] 查詢 propertyId = {}", propertyId);

        Optional<PropertyEntity> optionals = propertyRepository.findById(propertyId);
        if (optionals.isEmpty()) {
            log.warn("[MERCH-007] 依據 propertyId 查無資料");
            throw new DataNotFoundException("查無旅館資料");
        }

        PropertyEntity existingProperty = optionals.get();

        try {
            existingProperty.setName(merch007Tranrq.getName());
            existingProperty.setTel(merch007Tranrq.getTel());
            existingProperty.setPostalCode(merch007Tranrq.getPostalCode());
            existingProperty.setAddress(merch007Tranrq.getAddress());
            existingProperty.setBankAccount(merch007Tranrq.getBankAccount());
            existingProperty.setInfo(merch007Tranrq.getInfo());
            existingProperty.setCheckNotice(merch007Tranrq.getCheckNotice());
            existingProperty.setPetNotice(merch007Tranrq.getPetNotice());
            existingProperty.setPropertyNotice(merch007Tranrq.getPropertyNotice());

            propertyRepository.save(existingProperty);
            log.info("[MERCH-007] 更新成功，propertyId={}，更新欄位：{}", propertyId, merch007Tranrq);

        } catch (Exception e) {
            log.error("[MERCH-007] 更新旅館資訊失敗", e);
            throw new UpdateFailException("更新旅館資訊失敗");
        }

        MERCH007Tranrs merch007Tranrs = new MERCH007Tranrs();

        return new Res(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch007Tranrs
        );
    }
}
