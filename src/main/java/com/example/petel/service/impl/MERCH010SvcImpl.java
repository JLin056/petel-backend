package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.SellersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.SellersRepository;
import com.example.petel.service.MERCH010Svc;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH010SvcImpl implements MERCH010Svc {

    /**
     * SellersRepository
     */
    private final SellersRepository sellersRepository;
    /**
     * ObjectMapper
     */
    private final ObjectMapper objectMapper;

    /**
     * 修改商家會員資訊
     *
     * @param accountId   已登入帳號 ID
     * @param requestBody Req<MERCH010Tranrq>
     * @return Res<MERCH010Tranrs>
     * @throws DataNotFoundException, UpdateFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH010Tranrs> editSeller(String accountId, Req<MERCH010Tranrq> requestBody)
            throws DataNotFoundException, JsonMappingException, UpdateFailException {

        log.info("-------- [MERCH-010] 商家資訊修改 accountId = {} ----", accountId);

        // 1. 檢查並取得 Seller Entity
        // 使用 findByAccountId() 獲取完整的 Entity，以便進行更新。
        Optional<SellersEntity> optionalSeller = sellersRepository.findByAccountId(accountId);

        if (optionalSeller.isEmpty()) {
            log.warn("[MERCH-010] 查無此帳號對應的商家資料。AccountId={}", accountId);
            throw new DataNotFoundException("查無商家資料");
        }

        SellersEntity sellerToUpdate = optionalSeller.get();
        MERCH010Tranrq merch010Tranrq = requestBody.getTranrq();

        // 2. 使用 ObjectMapper 進行部分更新
        // 這會將 DTO 中有值的欄位對映到 Entity 上，同時保留 Entity 中其他欄位的值。
        objectMapper.updateValue(sellerToUpdate, merch010Tranrq);

        // 3. 執行保存 (更新)
        try {
            sellersRepository.save(sellerToUpdate);
            log.info("[MERCH-010] 商家會員資料修改成功。Seller ID={}", sellerToUpdate.getId());
        } catch (Exception e) {
            log.error("[MERCH-010] 修改商家會員資料失敗，原因：{}", e.getMessage(), e);
            // 這裡必須拋出 UpdateFailException，而不是 InsertFailException
            throw new UpdateFailException("修改商家會員資訊失敗");
        }

        // 4. 準備回傳結果
        MERCH010Tranrs merch010Tranrs = objectMapper.convertValue(sellerToUpdate, MERCH010Tranrs.class);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch010Tranrs
        );
    }
}
