package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PropertyRepository;
import com.example.petel.service.HOTEL005Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HOTEL005SvcImpl implements HOTEL005Svc {

    /**
     * PropertyRepository
     */
    private final PropertyRepository propertyRepository;

    /**
     * 查詢單筆旅館注意事項
     *
     * @param hotel005Tranrq Req<HOTEL005Tranrq> （sellerId)
     * @return Res<HOTEL005Tranrs>
     * @throws DataNotFoundException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<HOTEL005Tranrs> policies(Req<HOTEL005Tranrq> hotel005Tranrq) throws DataNotFoundException {
        log.info("-------- [HOTEL-005] 查詢單旅館注意事項 ---------");
        Long sellerId = hotel005Tranrq.getTranrq().getSellerId();
        log.info("[HOTEL-005] 查詢 sellerId = {}", sellerId);

        Optional<PropertyEntity> propertyOptional = propertyRepository.findBySellerId(sellerId);
        if (propertyOptional.isEmpty()) {
            log.warn("[HOTEL-005] 依據 sellerId 查無資料");
            throw new DataNotFoundException("依據旅館業者ID查無資料");
        }

        PropertyEntity property = propertyOptional.get();
        HOTEL005Tranrs hotel005Tranrs = new HOTEL005Tranrs();
        hotel005Tranrs.setNotice(property.getNotice());
        log.info("[HOTEL-005] 查詢成功，sellerId={}, notice={}", sellerId, property.getNotice());

        ResMwHeader resMwHeader = new ResMwHeader();
        Res<HOTEL005Tranrs> response = new Res<>();
        resMwHeader.setReturnCode(ReturnCodeAndDescEnum.SUCCESS.getCode());
        resMwHeader.setReturnDesc(ReturnCodeAndDescEnum.SUCCESS.getDesc());

        response.setMwHeader(resMwHeader);
        response.setTranrs(hotel005Tranrs);
        return response;
    }
}
