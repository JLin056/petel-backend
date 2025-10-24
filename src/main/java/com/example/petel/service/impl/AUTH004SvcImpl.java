package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.service.AUTH004Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class AUTH004SvcImpl implements AUTH004Svc {

    /** AccountsRepository */
    private final AccountsRepository accountsRepo;
    /** StringRedisTemplate */
    private final StringRedisTemplate redis;
    /** Reset password token prefix */
    private static final String RP_PREFIX = "RP:";
    /** NO_USER */
    private static final String NO_USER = "NO_USER";
    /** Token expired time */
    private static final Duration TOKEN_TTL = Duration.ofMinutes(15);
    /** Reset password page */
    private static final String FRONT_RESET_URL = "http://localhost:4200/resetPassword?token=";

    /**
     * 忘記密碼
     * @param req
     * @return
     */
    @Override
    public Res<AUTH004Tranrs> forgotPassword(Req<AUTH004Tranrq> req) {
        log.info("---- [AUTH-004] 忘記密碼 ----");
        String email = req.getTranrq().getEmail().trim().toLowerCase();
        String token = generateToken();
        String key = RP_PREFIX + token;

        String accountId = accountsRepo.findIdByEmailIgnoreCase(email);
        if (accountId != null) {
            redis.opsForValue().set(key, accountId, TOKEN_TTL);
            log.info("[AUTH-004] 建立 reset token 成功 accountId={}", accountId);
        } else {
            redis.opsForValue().set(key, NO_USER, TOKEN_TTL);
            log.info("[AUTH-004] email 不存在，仍建立 placeholder token 以隱藏存在性");
        }

        AUTH004Tranrs tranrs = new AUTH004Tranrs(FRONT_RESET_URL + token);
        return new Res<AUTH004Tranrs>(
            new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
            tranrs
        );
    }

    /**
     * 產 忘記密碼 token
     * @return
     */
    private String generateToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
