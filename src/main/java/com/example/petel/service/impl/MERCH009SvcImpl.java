package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.SellersEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.SellersRepository;
import com.example.petel.service.MERCH009Svc;
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
     * 新增會員資訊
     *
     * @param requestBody Req<MERCH009Tranrq> （sellerId)
     * @return Res<MERCH009Tranrs>
     * @throws InsertFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH009Tranrs> createSeller(Req<MERCH009Tranrq> requestBody) throws InsertFailException {

        log.info("-------- [MERCH-009] 新增商家資訊 ---------");
        MERCH009Tranrq merch009Tranrq = requestBody.getTranrq();

        String newSellerId = IdUtil.generateTableId("S", sellersRepository.findMaxId());
        log.info("[MERCH-009] 生成商家ID:{}", newSellerId);

        try {
            SellersEntity sellersEntity = new SellersEntity();
            sellersEntity.setId(newSellerId);
            sellersEntity.setAccountId(merch009Tranrq.getAccountId());
            sellersEntity.setName(merch009Tranrq.getName());
            sellersEntity.setBusinessCode(merch009Tranrq.getBusinessCode());
            sellersEntity.setMediaId(merch009Tranrq.getMediaId());
            sellersRepository.save(sellersEntity);
            log.info("[MERCH-009] 新增成功，新增欄位：{}", merch009Tranrq);

        } catch (Exception e) {
            log.error("[MERCH-009] 新增商家失敗", e);
            throw new InsertFailException("新增商家失敗" + e.getMessage());
        }

        MERCH009Tranrs merch009Tranrs = new MERCH009Tranrs();
        return new Res(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch009Tranrs
        );
    }
}
