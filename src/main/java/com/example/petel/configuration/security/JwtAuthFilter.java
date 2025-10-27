package com.example.petel.configuration.security;

import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.model.jwt.JwtUtil;
import com.example.petel.repository.AccountsRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
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
    private final AccountsRepository accountsRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        log.info("---- JWTAuthFilter ----");

        String uri = request.getRequestURI();
        if (uri.startsWith("/auth/login")
                || uri.startsWith("/auth/register")
                || uri.startsWith("/auth/refresh")
                || uri.startsWith("/auth/forgot")
                || uri.startsWith("/auth/reset")
                || uri.startsWith("/auth/verify")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (uri.startsWith("/ws")) {
            filterChain.doFilter(request, response); // 放行到下一個 filter / servlet
            return;
        }

        // CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtUtil.parseAccessToken(token);

            String accountId = claims.getSubject();
            String role = claims.get(JwtUtil.CLAIM_ROLE, String.class);
            Integer tokenVersion = jwtUtil.getTokenVersionFromClaims(claims);

            // 檢查必要欄位
            if (accountId == null || role == null || role.isBlank()) {
                log.warn("[JWTAuthFilter] JWT 缺少 sub/role/version");
                SecurityContextHolder.clearContext();
                SecurityErrorResponseWriter.writeError(
                        response,
                        HttpServletResponse.SC_UNAUTHORIZED,
                        ReturnCodeAndDescEnum.JWT_INVALID,
                        "JWT 缺少必要欄位");
                return;
            }

            // 驗證 role
            final String roleLower = role.trim().toLowerCase();
            if (!(roleLower.equals("user") || roleLower.equals("seller") || roleLower.equals("admin"))) {
                SecurityContextHolder.clearContext();
                SecurityErrorResponseWriter.writeError(
                        response,
                        HttpServletResponse.SC_UNAUTHORIZED,
                        ReturnCodeAndDescEnum.JWT_INVALID,
                        "不支援的角色");
                return;
            }

            // 驗證 token_version
            Integer tokenVersionDb = accountsRepository.findTokenVersionById(accountId);
            if (tokenVersionDb == null || !jwtUtil.matchTokenVersion(claims, tokenVersionDb)) {
                log.warn("[JWTAuthFilter] token_version 不一致, accountId={}, tokenVersion={}, dbVersion={}",
                        accountId, tokenVersion, tokenVersionDb);

                SecurityContextHolder.clearContext();
                SecurityErrorResponseWriter.writeError(
                        response,
                        HttpServletResponse.SC_UNAUTHORIZED,
                        ReturnCodeAndDescEnum.TOKEN_VERSION_MISMATCH,
                        "Token version 不正確，請重新登入");
                return;
            }

            // 建立 Authentication 存入 SecurityContext
            AccountPrincipal principal = new AccountPrincipal(accountId, roleLower, tokenVersion);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + roleLower.toUpperCase())));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            SecurityErrorResponseWriter.writeError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ReturnCodeAndDescEnum.JWT_EXPIRED,
                    "JWT 已過期");
        } catch (JwtProcessingException
                | io.jsonwebtoken.security.SignatureException
                | io.jsonwebtoken.MalformedJwtException
                | io.jsonwebtoken.UnsupportedJwtException
                | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            SecurityErrorResponseWriter.writeError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ReturnCodeAndDescEnum.JWT_INVALID,
                    "JWT 驗證失敗");
        } catch (Exception e) {
            log.error("[JWTAuthFilter] 未預期錯誤：", e);
            SecurityContextHolder.clearContext();
            SecurityErrorResponseWriter.writeError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ReturnCodeAndDescEnum.UNAUTHORIZED,
                    "未授權");
        }
    }

    /**
     * 解析 JWT Token
     * 
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
        return null;
    }
}
