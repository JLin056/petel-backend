package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
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

    /**
     * PropertyRepository
     */
    private final PropertyRepository propertyRepository;

    /**
     * 新增旅館資訊
     *
     * @param requestBody Req<MERCH008Tranrq> （propertyId)
     * @return Res<MERCH008Tranrs>
     * @throws DataNotFoundException,InsertFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH008Tranrs> insert(Req<MERCH008Tranrq> requestBody) throws DataNotFoundException, InsertFailException {

        log.info("-------- [MERCH-008] 新增旅館資訊 ---------");
        MERCH008Tranrq merch008Tranrq = requestBody.getTranrq();

        String newPropertyId = IdUtil.generateTableId("P", propertyRepository.findMaxId());
        log.info("[MERCH-008] 生成旅館ID:{}", newPropertyId);

        try {
            PropertyEntity propertyEntity = new PropertyEntity();
            propertyEntity.setId(newPropertyId);
            propertyEntity.setSellerId(merch008Tranrq.getSellerId());
            propertyEntity.setName(merch008Tranrq.getName());
            propertyEntity.setTel(merch008Tranrq.getTel());
            propertyEntity.setPostalCode(merch008Tranrq.getPostalCode());
            propertyEntity.setAddress(merch008Tranrq.getAddress());
            propertyEntity.setBankAccount(merch008Tranrq.getBankAccount());
            propertyEntity.setInfo(merch008Tranrq.getInfo());
            propertyEntity.setCheckNotice(merch008Tranrq.getCheckNotice());
            propertyEntity.setPetNotice(merch008Tranrq.getPetNotice());
            propertyEntity.setPropertyNotice(merch008Tranrq.getPropertyNotice());
            propertyRepository.save(propertyEntity);
            log.info("[MERCH-008] 新增成功，新增欄位：{}", merch008Tranrq);

        } catch (Exception e) {
            log.error("[MERCH-008] 新增旅館失敗", e);
            throw new InsertFailException("新增旅館失敗" + e.getMessage());
        }

        MERCH008Tranrs merch008Tranrs = new MERCH008Tranrs();
        return new Res(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch008Tranrs
        );
    }
}
