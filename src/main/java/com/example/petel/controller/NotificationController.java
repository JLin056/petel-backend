package com.example.petel.controller;

import com.example.petel.component.NotificationHub;
import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.UnauthorizedException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.model.jwt.JwtUtil;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.service.*;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Notification Controller
 * 處理通知相關的 API 請求
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
@RequestMapping("/notifications")
public class NotificationController extends BaseController {

    /** NOTIFY001Svc - 發送通知 */
    private final NOTIFY001Svc notify001Svc;

    /** NOTIFY002Svc - 查詢通知列表 */
    private final NOTIFY002Svc notify002Svc;

    /** NOTIFY003Svc - 標記通知為已讀 */
    private final NOTIFY003Svc notify003Svc;

    /** NOTIFY004Svc - 統計未讀通知數量 */
    private final NOTIFY004Svc notify004Svc;

    /** NOTIFY005Svc - 建立 SSE 連線 */
    private final NOTIFY005Svc notify005Svc;

    /** NOTIFY006Svc - 補發錯過的事件 */
    private final NOTIFY006Svc notify006Svc;

    /** JwtUtil - JWT 工具類 */
    private final JwtUtil jwtUtil;

    /** AccountsRepository - 帳號資料庫 */
    private final AccountsRepository accountsRepository;

    /** NotificationHub - SSE 連線管理 */
    private final NotificationHub notificationHub;

    /**
     * NOTIFY-001 發送通知
     * POST /notifications/send
     */
    @PostMapping(value = "/send")
    public Res<NOTIFY001Tranrs> notify001(
            @AuthenticationPrincipal AccountPrincipal authInfo,
            @Valid @RequestBody Req<NOTIFY001Tranrq> requestBody,
            Errors errors) throws InvalidInputException, UnauthorizedException {
        if (authInfo == null) {
            throw new UnauthorizedException("USER_NOT_LOGIN");
        }
        handleValidForDto(errors);
        return notify001Svc.notify001(authInfo.getAccountId(), requestBody);
    }

    /**
     * NOTIFY-002 查詢通知列表
     * GET /notifications/list
     */
    @GetMapping(value = "/list")
    public Res<NOTIFY002Tranrs> notify002(@AuthenticationPrincipal AccountPrincipal authInfo) throws UnauthorizedException {
        if (authInfo == null) {
            throw new UnauthorizedException("USER_NOT_LOGIN");
        }
        return notify002Svc.notify002(authInfo.getAccountId());
    }

    /**
     * NOTIFY-003 標記通知為已讀
     * POST /notifications/mark-read
     */
    @PostMapping(value = "/mark-read")
    public Res<NOTIFY003Tranrs> notify003(
            @AuthenticationPrincipal AccountPrincipal authInfo,
            @Valid @RequestBody Req<NOTIFY003Tranrq> requestBody,
            Errors errors) throws InvalidInputException, UnauthorizedException {
        if (authInfo == null) {
            throw new UnauthorizedException("USER_NOT_LOGIN");
        }
        handleValidForDto(errors);
        return notify003Svc.notify003(authInfo.getAccountId(), requestBody);
    }

    /**
     * NOTIFY-004 統計未讀通知數量
     * GET /notifications/unread-count
     */
    @GetMapping(value = "/unread-count")
    public Res<NOTIFY004Tranrs> notify004(@AuthenticationPrincipal AccountPrincipal authInfo) throws UnauthorizedException {
        if (authInfo == null) {
            throw new UnauthorizedException("USER_NOT_LOGIN");
        }
        return notify004Svc.notify004(authInfo.getAccountId());
    }

    /**
     * NOTIFY-005 建立 SSE 連線
     * GET /notifications/subscribe
     * 使用 Server-Sent Events (SSE) 進行即時推播
     *
     * 支援兩種驗證方式：
     * 1. 透過 Authorization header (標準方式)
     * 2. 透過 query parameter token (用於 SSE，因為 EventSource 不支援自定義 headers)
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter notify005(
            @AuthenticationPrincipal AccountPrincipal authInfo,
            @RequestParam(value = "token", required = false) String token) throws UnauthorizedException {

        String accountId = null;

        // 方式 1: 如果已經通過正常的 JWT Filter 驗證（從 Authorization header）
        if (authInfo != null) {
            accountId = authInfo.getAccountId();
            log.info("[NOTIFY-005] 使用 Authorization header 驗證，accountId={}", accountId);
        }
        // 方式 2: 嘗試從 query parameter 解析 token（用於 SSE EventSource）
        else if (token != null && !token.isBlank()) {
            try {
                // 解析 token
                Claims claims = jwtUtil.parseAccessToken(token);

                String claimAccountId = claims.getSubject();
                String role = claims.get(JwtUtil.CLAIM_ROLE, String.class);
                Integer tokenVersion = jwtUtil.getTokenVersionFromClaims(claims);

                // 檢查必要欄位
                if (claimAccountId == null || role == null || role.isBlank()) {
                    log.warn("[NOTIFY-005] Token 缺少必要欄位");
                    throw new UnauthorizedException("TOKEN_INVALID");
                }

                // 驗證 token_version
                Integer tokenVersionDb = accountsRepository.findTokenVersionById(claimAccountId);
                if (tokenVersionDb == null || !jwtUtil.matchTokenVersion(claims, tokenVersionDb)) {
                    log.warn("[NOTIFY-005] token_version 不一致, accountId={}, tokenVersion={}, dbVersion={}",
                            claimAccountId, tokenVersion, tokenVersionDb);
                    throw new UnauthorizedException("TOKEN_VERSION_MISMATCH");
                }

                accountId = claimAccountId;
                log.info("[NOTIFY-005] 使用 query parameter token 驗證成功，accountId={}", accountId);

            } catch (UnauthorizedException e) {
                throw e;
            } catch (Exception e) {
                log.error("[NOTIFY-005] Token 驗證失敗", e);
                throw new UnauthorizedException("TOKEN_INVALID");
            }
        }

        // 如果兩種方式都沒有提供有效的驗證資訊
        if (accountId == null) {
            log.warn("[NOTIFY-005] 未提供有效的驗證資訊");
            throw new UnauthorizedException("USER_NOT_LOGIN");
        }

        return notify005Svc.notify005(accountId);
    }

    /**
     * NOTIFY-006 補發錯過的事件
     * POST /notifications/resend-missed
     * 用於斷線重連後補發錯過的事件
     */
    @PostMapping(value = "/resend-missed")
    public Res<NOTIFY006Tranrs> notify006(
            @AuthenticationPrincipal AccountPrincipal authInfo,
            @Valid @RequestBody Req<NOTIFY006Tranrq> requestBody,
            Errors errors) throws InvalidInputException, UnauthorizedException {
        if (authInfo == null) {
            throw new UnauthorizedException("USER_NOT_LOGIN");
        }
        handleValidForDto(errors);
        return notify006Svc.notify006(authInfo.getAccountId(), requestBody);
    }

    /**
     * 清理當前帳號的所有 SSE 連線（用於測試/除錯）
     * DELETE /notifications/connections
     */
    @DeleteMapping(value = "/connections")
    public Res<Object> clearConnections(@AuthenticationPrincipal AccountPrincipal authInfo) throws UnauthorizedException {
        if (authInfo == null) {
            throw new UnauthorizedException("USER_NOT_LOGIN");
        }
        String accountId = authInfo.getAccountId();
        int count = notificationHub.getConnectionCount(accountId);
        notificationHub.removeAllConnections(accountId);
        log.info("[NotificationController] 已清理帳號 {} 的 {} 個連線", accountId, count);

        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("clearedConnections", count);
        return new Res<>(new com.example.petel.dto.ResMwHeader(com.example.petel.model.ReturnCodeAndDescEnum.SUCCESS), result);
    }
}
