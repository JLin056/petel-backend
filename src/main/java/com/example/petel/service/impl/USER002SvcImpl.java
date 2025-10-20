package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.UsersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.USER002Svc;
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
public class USER002SvcImpl implements USER002Svc {

    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** ObjectMapper */
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<USER002Tranrs> updateUser(String accountId, Req<USER002Tranrq> req)
            throws DataNotFoundException, JsonMappingException, InsertFailException {
        log.info("---- [USER-002] 修改會員資訊 accountId = {} ----", accountId);

        // 取該筆資料
        UsersEntity user = usersRepo.findByAccountId(accountId)
                .orElseThrow(() -> new DataNotFoundException("查無此帳號"));

        // 寫入更新資料
        USER002Tranrq tranrq = req.getTranrq();
        objectMapper.updateValue(user, tranrq);

        try {
            usersRepo.save(user);
            log.info("[USER-002] 修改會員資料成功 userId={}", user.getId());
        } catch (Exception e) {
            log.error("[USER-002] 新增會員資料失敗，原因：{}", e.getMessage(), e);
            throw new InsertFailException("修改會員資訊失敗");
        }

        USER002Tranrs tranrs = objectMapper.convertValue(user, USER002Tranrs.class);

        return new Res<USER002Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
