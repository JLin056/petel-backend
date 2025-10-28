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

    /**
     * 登出
     * @param request HttpServletRequest
     * @param resp HttpServletResponse
     * @return
     */
    @Override
    public Res<Object> logout(HttpServletRequest request, HttpServletResponse resp) {
        log.info("---- [AUTH-003] 登出 ----");

        ResponseCookie clearAccess = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        resp.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());

        // 清除 Refresh Token
        ResponseCookie clearRefresh = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/auth")
                .maxAge(0)
                .build();

        resp.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());

        log.info("[AUTH-003] 登出成功，Cookie 已清除");
        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                null
        );
    }
}
