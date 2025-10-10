package com.example.petel.service.impl;

import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.JwtUtil;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.service.AUTH003Svc;
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
     * @param resp HttpServletResponse
     * @return Res<Object>
     */
    @Override
    public Res<Object> logout(HttpServletResponse resp) {
        log.info("---- [AUTH-003] 登出 ----");

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
}
