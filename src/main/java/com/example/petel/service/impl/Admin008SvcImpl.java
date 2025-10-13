package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.AccountsEntity;
import com.example.petel.entity.UsersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.Admin008Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class Admin008SvcImpl implements Admin008Svc {

    private final UsersRepository usersRepository;
    private final AccountsRepository accountsRepository;

    /**
     * 刪除使用者（連同其關聯的帳號）
     * @param req Req<Admin008Tranrq>
     * @return Res<Admin008Tranrs>
     */
    @Override
    @Transactional
    public Res<Admin008Tranrs> deleteUser(Req<Admin008Tranrq> req) throws Exception, DataNotFoundException, DeleteFailException {
        log.info("-------- [ADMIN-008] 刪除使用者 ---------");
        Admin008Tranrq tranrq = req.getTranrq();
        Integer usersId = tranrq.getUsersId();

        log.info("[ADMIN-008] 刪除使用者 ID: {}", usersId);

        // 檢查使用者是否存在
        UsersEntity user = usersRepository.findById(usersId)
                .orElseThrow(() -> {
                    log.warn("[ADMIN-008] 使用者不存在，ID: {}", usersId);
                    return new DataNotFoundException("使用者不存在，ID: " + usersId);
                });

        try {
            // 取得使用者的帳號 ID
            Integer accountsId = user.getAccountsId();

            // 第一步：刪除使用者
            usersRepository.delete(user);
            log.info("[ADMIN-008] 使用者刪除成功，ID: {}", usersId);

            // 第二步：刪除關聯的帳號
            if (accountsId != null) {
                AccountsEntity account = accountsRepository.findById(accountsId.longValue())
                        .orElse(null);
                if (account != null) {
                    accountsRepository.delete(account);
                    log.info("[ADMIN-008] 帳號刪除成功，ID: {}", accountsId);
                } else {
                    log.warn("[ADMIN-008] 找不到關聯的帳號，ID: {}", accountsId);
                }
            }

            // 組裝回應
            Admin008Tranrs tranrs = new Admin008Tranrs();
            tranrs.setUsersId(usersId);
            tranrs.setMessage("使用者刪除成功，連同刪除關聯帳號");

            return new Res<>(
                    new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                    tranrs
            );
        } catch (DataAccessException e) {
            log.error("[ADMIN-008] 刪除使用者失敗，ID: {}, 錯誤訊息: {}", usersId, e.getMessage(), e);
            throw new DeleteFailException("刪除使用者失敗: " + e.getMessage());
        }
    }
}
