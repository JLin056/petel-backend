package com.example.petel.service.impl;

import com.example.petel.dto.MERCH011Tranrs;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.SellersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.repository.SellersRepository;
import com.example.petel.service.MERCH011Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH011SvcImpl implements MERCH011Svc {

    /**
     * SellersRepository
     */
    private final SellersRepository sellersRepository;
    /**
     * AccountsRepository
     */
    private final AccountsRepository accountsRepository;

    /**
     * 查詢商家會員資訊
     *
     * @param accountId
     * @return
     * @throws DataNotFoundException
     */
    @Override
    public Res<MERCH011Tranrs> getSellerInfo(String accountId) throws DataNotFoundException {
        log.info("---- [MERCH-011] 查詢商家會員資訊 ----");

        SellersEntity sellersEntity = sellersRepository.findByAccountId(accountId)
                .orElseThrow(() -> new DataNotFoundException("查無商家會員資訊"));

        String email = accountsRepository.findEmailById(accountId);

        MERCH011Tranrs merch011Tranrs = new MERCH011Tranrs();
        merch011Tranrs.setId(sellersEntity.getId());
        merch011Tranrs.setAccountId(accountId);
        merch011Tranrs.setName(sellersEntity.getName());
        merch011Tranrs.setPhone(sellersEntity.getPhone());
        merch011Tranrs.setEmail(email);
        merch011Tranrs.setMediaId(sellersEntity.getMediaId());

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch011Tranrs
        );
    }

    ;
}

