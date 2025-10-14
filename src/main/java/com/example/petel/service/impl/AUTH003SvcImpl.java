package com.example.petel.service.impl;

import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.AccountsEntity;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.JwtUtil;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.service.AUTH003Svc;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AUTH003SvcImpl implements AUTH003Svc {

    /** AccountsRepository */
    private final AccountsRepository accountsRepo;
    /** PasswordEncoder */
    private final PasswordEncoder passwordEncoder;
    /** JwtUtil */
    private final JwtUtil jwtUtil;

    /**
     * 登出
     * @param request HttpServletRequest
     * @param resp HttpServletResponse
     * @return
     */
    @Override
    public Res<Object> logout(HttpServletRequest request, HttpServletResponse resp) {
        log.info("---- [AUTH-003] 登出 ----");

        String token = extractTokenFromCookie(request, "access_token");

        if (token != null && jwtUtil.validateToken(token)) {
            try {
                Claims claims = jwtUtil.getClaims(token);
                String accountId = claims.getSubject();

                AccountsEntity account = accountsRepo.findById(accountId).orElse(null);
                if (account != null) {
                    Integer oldTokenVersion = account.getTokenVersion();
                    account.setTokenVersion(oldTokenVersion + 1);
                    accountsRepo.save(account);
                    log.info("[AUTH-003] token_version 從 {} → {} (accountId={})",
                            oldTokenVersion, account.getTokenVersion(), accountId);
                } else {
                    log.warn("[AUTH-003] 找不到帳號 accountId={}", accountId);
                }

            } catch (JwtProcessingException e) {
                log.error("[AUTH-003] 登出時解析 token 失敗：{}", e.getMessage(), e);
                throw new RuntimeException(e);
            }
        } else {
            log.warn("[AUTH-003] Token 不存在或無效，仍清除 cookie");
        }

        // 清除 access Token
        ResponseCookie clearAccess = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        // 清除 Refresh Token
        ResponseCookie clearRefresh = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(0)
                .build();

        resp.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());
        resp.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());

        log.info("[AUTH-003] 登出成功，Cookie 已清除");
        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                null
        );
    }

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie: request.getCookies()){
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
