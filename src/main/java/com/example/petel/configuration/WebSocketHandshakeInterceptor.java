package com.example.petel.configuration;

import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.model.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.HttpCookie;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        try {
            List<String> cookieHeaders = request.getHeaders().getOrDefault("Cookie", List.of());
            String token = null;

            for (String cookieHeader : cookieHeaders) {
                for (HttpCookie c : HttpCookie.parse(cookieHeader)) {
                    if ("access_token".equals(c.getName())) {
                        token = c.getValue();
                        break;
                    }
                }
            }

            if (token == null || token.isBlank()) {
                log.warn("[WS HS] 未找到 access_token cookie");
                return false;
            }

            Claims claims = jwtUtil.getClaims(token);
            String accountId = claims.getSubject();
            String role = claims.get("role", String.class);
            Integer tokenVersion = claims.get("token_version", Integer.class);

            if (accountId == null || role == null || tokenVersion == null) {
                log.warn("[WS HS] JWT 缺少必要欄位");
                return false;
            }

            attributes.put("WS_PRINCIPAL", new AccountPrincipal(accountId, role, tokenVersion));

            log.info("[WS HS] 握手驗證成功 accountId={}, role={}", accountId, role);
            return true;

        } catch (Exception e) {
            log.warn("[WS HS] 握手驗證失敗：{}", e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }
}
