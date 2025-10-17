package com.example.petel.model.jwt;

import com.example.petel.exception.JwtProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Getter
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration;

    /**
     * 產生 Access Token
     * @param accountId 帳號ID
     * @param email Email
     * @param role 角色
     * @return
     */
    public String generateAccessToken(String accountId, String email, String role, Integer tokenVersion)
            throws JwtProcessingException {
        log.info("---- [JWT] generateAccessToken ----");
        try {
            String token = Jwts.builder()
                    .setSubject(accountId)
                    .claim("email", email)
                    .claim("role", role)
                    .claim("token_version", tokenVersion)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
            log.info("[JWT] AccessToken 建立成功 accountId={} role={}", accountId, role);
            return token;
        } catch (Exception e) {
            log.error("[JWT] AccessToken 建立失敗 accountId={} role={}，原因={}", accountId, role, e.getMessage(), e);
            throw new JwtProcessingException("產生 AccessToken 失敗");
        }

    }

    /**
     * 產生 Refresh Token
     * @param accountId 帳號ID
     * @return Token
     */
    public String generateRefreshToken(String accountId, Integer tokenVersion) throws JwtProcessingException {
        log.info("---- [JWT] generateRefreshToken ----");
        try {
            String token = Jwts.builder()
                    .setSubject(accountId)
                    .claim("type", "refresh")
                    .claim("token_version", tokenVersion)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
            log.info("[JWT] RefreshToken 建立成功 accountId = {}", accountId);
            return token;
        } catch (Exception e) {
            log.error("[JWT] RefreshToken 建立失敗 accountId = {}，原因={}", accountId, e.getMessage(), e);
            throw new JwtProcessingException("RefreshToken 建立失敗");
        }
    }

    /**
     * 解析 Token 拿出的 payload
     * @param token Token
     * @return Payload
     */
    public Claims getClaims(String token) throws JwtProcessingException {
        log.info("---- [JWT] getClaims ----");
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            log.info("[JWT] getClaims 解析成功 subject={} role={}", claims.getSubject(), claims.get("role"));
            return claims;
        } catch (Exception e) {
            log.error("[JWT] getClaims 解析失敗：{}", e.getMessage(), e);
            throw new JwtProcessingException("JWT 解析失敗");
        }
    }

    /**
     * 取得簽章金鑰（用 jwt.secret 生成）
     * @return 簽章
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
