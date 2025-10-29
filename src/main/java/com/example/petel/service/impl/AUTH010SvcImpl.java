package com.example.petel.service.impl;

import com.example.petel.dto.AUTH010Tranrs;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.entity.AccountsEntity;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.JwtUtil;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.service.AUTH010Svc;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AUTH010SvcImpl implements AUTH010Svc {

    /** JwtUtil */
    private final JwtUtil jwtUtil;
    /** AccountsRepository */
    private final AccountsRepository accountsRepo;

    @Override
    public Res<AUTH010Tranrs> refresh(String refreshToken, HttpServletResponse resp) throws JwtProcessingException {
        log.info("---- [AUTH-010] 更新 Refresh Token ----");

        ResMwHeader resMwHeader = new ResMwHeader();
        if (refreshToken == null || refreshToken.isBlank()) {
            resMwHeader.setReturnCode(ReturnCodeAndDescEnum.UNAUTHORIZED.getCode());
            resMwHeader.setReturnDesc("缺少 Refresh Token");
            return new Res<>(resMwHeader, null);
        }

        try {
            Claims claims = jwtUtil.parseRefreshToken(refreshToken);
            String accountId = claims.getSubject();
            int tokenVersionFromClaims = jwtUtil.getTokenVersionFromClaims(claims);

            AccountsEntity accountsEntity = accountsRepo.findById(accountId)
                    .orElseThrow(() -> new JwtProcessingException("查無用戶資訊"));

            int tokenVersionDb = Optional.ofNullable(accountsEntity.getTokenVersion()).orElse(0);

            if (tokenVersionFromClaims != tokenVersionDb) {
                resMwHeader.setReturnCode(ReturnCodeAndDescEnum.TOKEN_VERSION_MISMATCH.getCode());
                resMwHeader.setReturnDesc("請重新登入");
                return new Res<>(resMwHeader, null);
            }

            String role = accountsEntity.getRole();
            String email = accountsEntity.getEmail();
            String newAccessToken = jwtUtil.generateAccessToken(accountId, email, role, tokenVersionDb);
            String newRefreshToken = jwtUtil.generateRefreshToken(accountId, tokenVersionDb);

            ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/auth")
                    .maxAge(Duration.ofMillis(jwtUtil.getRefreshTokenExpiration()))
                    .build();
            resp.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return new Res<>(
                    new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                    new AUTH010Tranrs(newAccessToken)
            );

        } catch (JwtProcessingException
                 | io.jsonwebtoken.ExpiredJwtException
                 | io.jsonwebtoken.security.SignatureException
                 | io.jsonwebtoken.MalformedJwtException
                 | IllegalArgumentException e) {
            // 可預期的未授權
            clearRtCookie(resp); // RT 無效/過期 → 清掉避免一直打
            log.warn("[AUTH-010] JWT 驗證失敗：{}", e.getMessage());
            resMwHeader.setReturnCode(ReturnCodeAndDescEnum.UNAUTHORIZED.getCode());
            resMwHeader.setReturnDesc("JWT 無效 或 未登入");
            return new Res<>(resMwHeader, null);
        } catch (Exception e) {
            // 其他非預期錯誤 → 500
            log.error("[AUTH-010] 伺服器錯誤：{}", e.getMessage(), e);
            resMwHeader.setReturnCode(ReturnCodeAndDescEnum.S9999.getCode()); // 例如 "9999"
            resMwHeader.setReturnDesc("系統異常");
            return new Res<>(resMwHeader, null);
        }
    }

    /**
     * 清除 cookie
     * @param resp
     */
    private void clearRtCookie(HttpServletResponse resp) {
        ResponseCookie cleared = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        resp.addHeader(HttpHeaders.SET_COOKIE, cleared.toString());
    }
}
