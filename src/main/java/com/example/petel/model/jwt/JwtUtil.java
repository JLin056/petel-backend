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
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

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

    public static final String CLAIM_TYP = "typ";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_TV = "tv"; // token_version

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
            Instant now = Instant.now();
            String token = Jwts.builder()
                    .setSubject(accountId)
                    .claim(CLAIM_TYP, "access")
                    .claim(CLAIM_EMAIL, email)
                    .claim(CLAIM_ROLE, role)
                    .claim(CLAIM_TV, tokenVersion)
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plusMillis(accessTokenExpiration)))
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
            Instant now = Instant.now();
            String token = Jwts.builder()
                    .setSubject(accountId)
                    .claim(CLAIM_TYP, "refresh")
                    .claim(CLAIM_TV, tokenVersion)
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plusMillis(refreshTokenExpiration)))
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
     * 解析並確定是 Access Token
     * @param token
     * @return
     * @throws JwtProcessingException
     */
    public Claims parseAccessToken(String token) throws JwtProcessingException {
        Claims claims = getClaims(token);
        String typ = claims.get(CLAIM_TYP, String.class);
        if (!"access".equals(typ)) {
            throw new JwtProcessingException("Token 類型錯誤，需為 access");
        }
        return claims;
    }


    /**
     * 解析並確定是 Refresh Token
     * @param token
     * @return
     * @throws JwtProcessingException
     */
    public Claims parseRefreshToken(String token) throws JwtProcessingException {
        Claims claims = getClaims(token);
        String typ = claims.get(CLAIM_TYP, String.class);
        if (!"refresh".equals(typ)) {
            throw new JwtProcessingException("Token 類型錯誤，需為 refresh");
        }
        return claims;
    }

    /**
     * 從 Claims 取出 token_version
     * @param claims
     * @return
     */
    public int getTokenVersionFromClaims(Claims claims) {
        Integer tokenVersion = claims.get(CLAIM_TV, Integer.class);
        return tokenVersion == null ? 0 : tokenVersion;
    }

    /**
     * claims 是否與 DB 的 token_version 一致
     * @param claims
     * @param tokenVersionInDb
     * @return
     */
    public boolean matchTokenVersion(Claims claims, int tokenVersionInDb) {
        Integer tv = claims.get(CLAIM_TV, Integer.class);
        return Objects.equals(tv, tokenVersionInDb);
    }

    /**
     * 取得簽章金鑰（用 jwt.secret 生成）
     * @return 簽章
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
