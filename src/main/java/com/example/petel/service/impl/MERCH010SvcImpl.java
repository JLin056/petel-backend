package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.SellersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.SellersRepository;
import com.example.petel.service.MERCH010Svc;
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
     * 修改會員資訊
     *
     * @param requestBody Req<MERCH010Tranrq> （sellerId)
     * @return Res<MERCH010Tranrs>
     * @throws DataNotFoundException,UpdateFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH010Tranrs>  editSeller(Req<MERCH010Tranrq> requestBody) throws DataNotFoundException, UpdateFailException {
        log.info("-------- [MERCH-010] 商家資訊修改 ---------");

        MERCH010Tranrq merch010Tranrq = requestBody.getTranrq();
        String sellerId = merch010Tranrq.getId();
        log.info("[MERCH-010] 查詢 sellerId = {}", sellerId);

        Optional<SellersEntity> optionals = sellersRepository.findById(sellerId);
        if (optionals.isEmpty()) {
            log.warn("[MERCH-010] 依據 sellerId 查無資料");
            throw new DataNotFoundException("查無商家資料");
        }

        SellersEntity existingSeller = optionals.get();

        try {
            existingSeller.setId(merch010Tranrq.getId());
            existingSeller.setAccountId(merch010Tranrq.getAccountId());
            existingSeller.setName(merch010Tranrq.getName());
            existingSeller.setBusinessCode(merch010Tranrq.getBusinessCode());
            existingSeller.setMediaId(merch010Tranrq.getMediaId());
            sellersRepository.save(existingSeller);
            log.info("[MERCH-010] 更新成功，sellerId={}，更新欄位：{}", sellerId, merch010Tranrq);

        } catch (Exception e) {
            log.error("[MERCH-010] 更新商家資訊失敗", e);
            throw new UpdateFailException("更新商家資訊失敗");
        }

        MERCH010Tranrs merch010Tranrs = new MERCH010Tranrs();

        return new Res(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch010Tranrs
        );
    }
}
