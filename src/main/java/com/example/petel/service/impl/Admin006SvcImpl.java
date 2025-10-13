package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PropertyRepository;
import com.example.petel.service.Admin006Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class Admin006SvcImpl implements Admin006Svc {

    private final PropertyRepository propertyRepository;

    /**
     * 刪除旅館
     * @param req Req<Admin006Tranrq>
     * @return Res<Admin006Tranrs>
     */
    @Override
    @Transactional
    public Res<Admin006Tranrs> deleteHotel(Req<Admin006Tranrq> req) throws Exception, DataNotFoundException, DeleteFailException {
        log.info("-------- [ADMIN-006] 刪除旅館 ---------");
        Admin006Tranrq tranrq = req.getTranrq();
        Integer propertyId = tranrq.getPropertyId();

        log.info("[ADMIN-006] 刪除旅館 ID: {}", propertyId);

        // 檢查旅館是否存在
        PropertyEntity property = propertyRepository.findById(Long.valueOf(propertyId))
                .orElseThrow(() -> {
                    log.warn("[ADMIN-006] 旅館不存在，ID: {}", propertyId);
                    return new DataNotFoundException("旅館不存在，ID: " + propertyId);
                });

        try {
            // 執行刪除
            propertyRepository.delete(property);
            log.info("[ADMIN-006] 旅館刪除成功，ID: {}", propertyId);

            // 組裝回應
            Admin006Tranrs tranrs = new Admin006Tranrs();
            tranrs.setPropertyId(propertyId);
            tranrs.setMessage("旅館刪除成功");

            return new Res<>(
                    new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                    tranrs
            );
        } catch (DataAccessException e) {
            log.error("[ADMIN-006] 刪除旅館失敗，ID: {}, 錯誤訊息: {}", propertyId, e.getMessage(), e);
            throw new DeleteFailException("刪除旅館失敗: " + e.getMessage());
        }
    }
}
