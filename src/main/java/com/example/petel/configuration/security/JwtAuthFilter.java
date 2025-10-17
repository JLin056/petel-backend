package com.example.petel.configuration.security;

import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    /** JwtUtil */
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("---- JWTAuthFilter ----");
        String token = resolveToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtUtil.getClaims(token);

            String accountId = claims.getSubject();
            String role = claims.get("role", String.class);
            if (role == null || role.isBlank()) {
                log.warn("[JWTAuthFilter] JWT 缺少 role");
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                throw new JwtProcessingException("JWT token 缺少 role");
            }

            // 建立 Authentication 存入 SecurityContext
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(accountId, null, authorities);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 過期時間
            if (claims.getExpiration() != null) {
                request.setAttribute("JWT_EXP", claims.getExpiration().toInstant());
            }

        } catch (Exception e) {
            log.warn("[JWTAuthFilter] 錯誤 = {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }


    /**
     * 解析 JWT Token
     * @param req
     * @return
     */
    private String resolveToken(HttpServletRequest req) {
        // Authorization header
        // 先用 header 內的，沒有再取 cookie
        String h = req.getHeader("Authorization");
        if (h != null) {
            String auth = h.trim();
            if (auth.toLowerCase().startsWith("bearer ")) {
                String candidate = auth.substring(7).trim();
                if (!candidate.isEmpty()) {
                    return candidate;
                }
            }
        }

        // Cookie: access_token
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("access_token".equals(c.getName())) {
                    String value = c.getValue();
                    if (value != null && !value.isBlank()) {
                        String candidate = value.trim();
                        if (!candidate.isBlank()) {
                            return candidate;
                        }
                    }
                }
            }
        }

        return null;
    }
}
