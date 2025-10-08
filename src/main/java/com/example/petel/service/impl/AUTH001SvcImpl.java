package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.AccountsEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.service.AUTH001Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AUTH001SvcImpl implements AUTH001Svc {

    /** AccountsRepository */
    private final AccountsRepository accountsRepo;
    /** PasswordEncoder */
    private final PasswordEncoder passwordEncoder;

    /**
     * 註冊
     * @param req Req<AUTH001Tranrq>（email, password, role）
     * @return Res<AUTH001Tranrs>
     * @throws InsertFailException 註冊失敗
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<AUTH001Tranrs> register(Req<AUTH001Tranrq> req) throws InsertFailException {
        log.info("-------- [AUTH-001] 註冊 ---------");
        AUTH001Tranrq tranrq = req.getTranrq();
        // 驗證 email 是否已被使用
        String email = tranrq.getEmail().trim().toLowerCase();

        if (accountsRepo.existsByEmailIgnoreCase(email)){
            log.warn("[AUTH-001] Email 已被使用：{}", email);
            throw new InsertFailException("Email 已被使用");
        }

        AccountsEntity accountsEntity = new AccountsEntity();
        accountsEntity.setEmail(email);
        accountsEntity.setPassword(passwordEncoder.encode(tranrq.getPassword()));  // 密碼雜湊
        accountsEntity.setRole(tranrq.getRole());
        accountsEntity.setStatus("active");

        try {
            accountsRepo.save(accountsEntity);
            log.info("[AUTH-001] 註冊成功，帳號ID＝{}，email＝{}",
                    accountsEntity.getAccountId(), email);
        } catch (Exception e) {
            log.error("[AUTH-001] 註冊失敗，email＝{}, error={}", email, e.getMessage());
            throw new InsertFailException("註冊失敗");
        }

        return new Res<AUTH001Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                new AUTH001Tranrs(accountsEntity.getAccountId())
        );
    }
}
