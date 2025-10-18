package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.UsersEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.USER001Svc;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class USER001SvcImpl implements USER001Svc {

    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** ObjectMapper */
    private final ObjectMapper objectMapper;

    @Transactional
    @Override
    public Res<USER001Tranrs> createUser(String accountId, Req<USER001Tranrq> req)
            throws InsertFailException {
        log.info("---- [USER-001] 新增會員資訊 ----");
        log.info("[USER-001] accountId={}, req={}", accountId, req);

        // 檢查 Account ID 是否存在
        if (usersRepo.existsByAccountId(accountId)) {
            log.warn("[USER-001] 該帳號已建立會員資訊，accountId={}", accountId);
            throw new InsertFailException("該帳號已建立會員資訊");
        }

        String maxId = usersRepo.findMaxId();
        String userId = IdUtil.generateTableId("U", maxId);
        log.info("[USER-001] 新會員ID 產生完成 userId={} (maxId={})", userId, maxId);

        USER001Tranrq tranrq = req.getTranrq();
        UsersEntity entity = new UsersEntity();
        entity.setId(userId);
        entity.setAccountId(accountId);
        entity.setName(tranrq.getName());
        entity.setPhone(tranrq.getPhone());
        entity.setMediaId(tranrq.getMediaId());

        try {
            usersRepo.save(entity);
            log.info("[USER-001] 新增會員資料成功 userId={}", userId);
        } catch (Exception e) {
            log.error("[USER-001] 新增會員資料失敗，原因：{}", e.getMessage(), e);
            throw new InsertFailException("新增會員資訊失敗");
        }

        USER001Tranrs tranrs = objectMapper.convertValue(entity, USER001Tranrs.class);

        return new Res<USER001Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
