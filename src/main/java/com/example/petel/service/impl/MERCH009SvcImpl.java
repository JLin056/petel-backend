package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.SellersEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.SellersRepository;
import com.example.petel.service.MERCH009Svc;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH009SvcImpl implements MERCH009Svc {

    /**
     * SellersRepository
     */
    private final SellersRepository sellersRepository;
    /**
     * ObjectMapper
     */
    private final ObjectMapper objectMapper;

    /**
     * 新增會員資訊
     *
     * @param requestBody Req<MERCH009Tranrq> （sellerId)
     * @return Res<MERCH009Tranrs>
     * @throws InsertFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH009Tranrs> createSeller(String accountId, Req<MERCH009Tranrq> requestBody)
            throws InsertFailException {

        log.info("-------- [MERCH-009] 新增商家資訊 ---------");
        log.info("[USER-001] accountId={}, req={}", accountId, requestBody);
        MERCH009Tranrq merch009Tranrq = requestBody.getTranrq();

        if (sellersRepository.existsByAccountId(accountId)) {
            log.warn("[MERCH-009] 該帳號已是商家會員，accountId={}", accountId);
            throw new InsertFailException("該帳號已是商家會員，無需重複創建");
        }

        String maxId = sellersRepository.findMaxId();
        String newSellerId = IdUtil.generateTableId("S", maxId);
        log.info("[MERCH-009] 新商家會員ID產生完成，newSellerId={} (maxId={})", newSellerId, maxId);

        SellersEntity sellersEntity = new SellersEntity();
        sellersEntity.setId(newSellerId);
        sellersEntity.setAccountId(accountId);
        sellersEntity.setName(merch009Tranrq.getName());
        sellersEntity.setPhone(merch009Tranrq.getPhone());
        sellersEntity.setMediaId(merch009Tranrq.getMediaId());
        try {
            sellersRepository.save(sellersEntity);
            log.info("[MERCH-009] 新增成功，新增欄位：{}", merch009Tranrq);

        } catch (Exception e) {
            log.error("[MERCH-009] 新增商家失敗", e);
            throw new InsertFailException("新增商家失敗" + e.getMessage());
        }

        MERCH009Tranrs tranrs = objectMapper.convertValue(sellersEntity, MERCH009Tranrs.class);
        MERCH009Tranrs merch009Tranrs = new MERCH009Tranrs();
        return new Res(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
