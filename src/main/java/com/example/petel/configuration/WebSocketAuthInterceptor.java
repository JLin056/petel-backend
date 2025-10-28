package com.example.petel.configuration;


import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.model.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    /**
     * 驗證
     * @param message
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) return message;

        StompCommand command = accessor.getCommand();

        if (command == StompCommand.CONNECT || command == StompCommand.SUBSCRIBE || command == StompCommand.SEND){
            String authorization = nativeHeader(accessor, "Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                log.warn("[WS] 缺少 Authorization Bearer，拒絕：command={}", command);
                return null;
            }

            String token = authorization.substring(7);
            try {
                Claims claims = jwtUtil.getClaims(token);
                String accountId = claims.getSubject();
                String role = claims.get("role", String.class);
                Integer tokenVersion = claims.get("token_version", Integer.class);

                if (accountId == null || role == null || tokenVersion == null) {
                    log.warn("[WS] JWT 欄位不完整，拒絕：command={}", command);
                    return null;
                }

                AccountPrincipal principal = new AccountPrincipal(accountId, role, tokenVersion);
                accessor.setUser(principal);

                accessor.getSessionAttributes().put("WS_PRINCIPAL", principal);

                if (command == StompCommand.CONNECT) {
                    log.info("[WS] CONNECT 驗證成功 accountId={}, role={}", accountId, role);
                }
            } catch (Exception e) {
                log.warn("[WS] JWT 驗證失敗：{}，command={}", e.getMessage(), command);
                return null;
            }
        }

        return message;
    }

    /**
     * 取 header
     * @param acc
     * @param name
     * @return
     */
    private String nativeHeader(StompHeaderAccessor acc, String name) {
        List<String> list = acc.getNativeHeader(name);
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}
