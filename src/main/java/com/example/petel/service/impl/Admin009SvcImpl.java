package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.AccountsEntity;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.entity.SellersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.repository.PropertyRepository;
import com.example.petel.repository.SellersRepository;
import com.example.petel.service.Admin009Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Admin009SvcImpl implements Admin009Svc {

    private final SellersRepository sellersRepository;
    private final PropertyRepository propertyRepository;
    private final AccountsRepository accountsRepository;

    /**
     * 刪除賣家（連同其相關的所有物業和帳號）
     * @param req Req<Admin009Tranrq>
     * @return Res<Admin009Tranrs>
     */
    @Override
    @Transactional
    public Res<Admin009Tranrs> deleteSeller(Req<Admin009Tranrq> req) throws Exception, DataNotFoundException, DeleteFailException {
        log.info("-------- [ADMIN-009] 刪除賣家 ---------");
        Admin009Tranrq tranrq = req.getTranrq();
        Integer sellersId = tranrq.getSellersId();

        log.info("[ADMIN-009] 刪除賣家 ID: {}", sellersId);

        // 檢查賣家是否存在
        SellersEntity seller = sellersRepository.findById(sellersId)
                .orElseThrow(() -> {
                    log.warn("[ADMIN-009] 賣家不存在，ID: {}", sellersId);
                    return new DataNotFoundException("賣家不存在，ID: " + sellersId);
                });

        try {
            // 取得賣家的帳號 ID
            Integer accountsId = seller.getAccountsId();

            // 查詢該賣家的所有物業
            List<PropertyEntity> properties = propertyRepository.findBySellerId(sellersId);
            int propertiesCount = properties.size();

            log.info("[ADMIN-009] 找到 {} 筆相關物業", propertiesCount);

            // 第一步：刪除所有相關物業
            if (!properties.isEmpty()) {
                propertyRepository.deleteAll(properties);
                log.info("[ADMIN-009] 已刪除 {} 筆物業", propertiesCount);
            }

            // 第二步：刪除賣家
            sellersRepository.delete(seller);
            log.info("[ADMIN-009] 賣家刪除成功，ID: {}", sellersId);

            // 第三步：刪除關聯的帳號
            if (accountsId != null) {
                AccountsEntity account = accountsRepository.findById(accountsId.longValue())
                        .orElse(null);
                if (account != null) {
                    accountsRepository.delete(account);
                    log.info("[ADMIN-009] 帳號刪除成功，ID: {}", accountsId);
                } else {
                    log.warn("[ADMIN-009] 找不到關聯的帳號，ID: {}", accountsId);
                }
            }

            // 組裝回應
            Admin009Tranrs tranrs = new Admin009Tranrs();
            tranrs.setSellersId(sellersId);
            tranrs.setDeletedPropertiesCount(propertiesCount);
            tranrs.setMessage("賣家刪除成功，連同刪除 " + propertiesCount + " 筆物業及關聯帳號");

            return new Res<>(
                    new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                    tranrs
            );
        } catch (DataAccessException e) {
            log.error("[ADMIN-009] 刪除賣家失敗，ID: {}, 錯誤訊息: {}", sellersId, e.getMessage(), e);
            throw new DeleteFailException("刪除賣家失敗: " + e.getMessage());
        }
    }
}
