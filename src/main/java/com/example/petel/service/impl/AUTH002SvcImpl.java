package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.AccountsEntity;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.JwtUtil;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.service.AUTH002Svc;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class AUTH002SvcImpl implements AUTH002Svc {

    /** AccountsRepository */
    private final AccountsRepository accountsRepo;
    /** PasswordEncoder */
    private final PasswordEncoder passwordEncoder;
    /** JwtUtil */
    private final JwtUtil jwtUtil;

    /**
     * 登入
     * @param req Request
     * @param resp httpServletResponse
     * @return Res
     * @throws InvalidInputException input 錯誤
     * @throws JwtProcessingException JWT 錯誤
     */
    @Override
    public Res<AUTH002Tranrs> login(Req<AUTH002Tranrq> req, HttpServletResponse resp)
            throws InvalidInputException, JwtProcessingException {
        log.info("---- [AUTH-002] 登入 ----");
        AUTH002Tranrq tranrq = req.getTranrq();
        String email = tranrq.getEmail().trim().toLowerCase();

        // 取得該 Email 資訊，若無則 帳號或密碼錯誤
        AccountsEntity accountsEntity = accountsRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    log.warn("[AUTH-002] 查無此帳號 email={}", email);
                    return new InvalidInputException("帳號或密碼錯誤");
                });

        log.debug("[AUTH-002] 查得帳號資訊 accountId={}, status={}, role={}",
                accountsEntity.getAccountId(), accountsEntity.getStatus(), accountsEntity.getRole());

        // 信箱驗證
        if (!"active".equalsIgnoreCase(accountsEntity.getStatus())) {
            log.warn("[AUTH-002] 信箱還未驗證 email={}", email);
            throw new InvalidInputException("信箱還未驗證");
        }

        // 密碼錯誤
        if (!passwordEncoder.matches(tranrq.getPassword(), accountsEntity.getPassword())) {
            log.warn("[AUTH-002] 密碼不正確 email={}", email);
            throw new InvalidInputException("帳號或密碼錯誤");
        }

        log.info("[AUTH-002] 帳號驗證成功 accountId={}", accountsEntity.getAccountId());

        // 產 accessToken
        log.debug("[AUTH-002] 開始產生 AccessToken...");
        String accessToken = jwtUtil.generateAccessToken(
                accountsEntity.getAccountId(),
                accountsEntity.getEmail(),
                accountsEntity.getRole()
        );
        log.info("[AUTH-002] AccessToken 產生成功 accountId={}", accountsEntity.getAccountId());

        // 產 refreshToken
        log.debug("[AUTH-002] 開始產生 RefreshToken...");
        String refreshToken = jwtUtil.generateRefreshToken(accountsEntity.getAccountId());
        log.info("[AUTH-002] RefreshToken 產生成功 accountId={}", accountsEntity.getAccountId());


        log.debug("[AUTH-002] 設定 HttpOnly Cookie...");
        ResponseCookie access = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(false)             // HTTP
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMillis(jwtUtil.getAccessTokenExpiration()))
                .build();

        ResponseCookie refresh = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)             // HTTP
                .sameSite("Strict")
                .path("/auth")
                .maxAge(Duration.ofMillis(jwtUtil.getRefreshTokenExpiration()))
                .build();

        resp.addHeader(HttpHeaders.SET_COOKIE, access.toString());
        resp.addHeader(HttpHeaders.SET_COOKIE, refresh.toString());
        log.info("[AUTH-002] Cookie 寫入完成");

        log.info("[AUTH-002] 登入成功 accountId={}, role={}, email={}",
                accountsEntity.getAccountId(), accountsEntity.getRole(), accountsEntity.getEmail());

        return new Res<AUTH002Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                new AUTH002Tranrs(accountsEntity.getAccountId(), accountsEntity.getEmail(), accountsEntity.getRole())
        );
    }
}
