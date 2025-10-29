package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.UnauthorizedException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Notification Controller
 * 處理通知相關的 API 請求
 */
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
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter notify005(@AuthenticationPrincipal AccountPrincipal authInfo) throws UnauthorizedException {
        if (authInfo == null) {
            throw new UnauthorizedException("USER_NOT_LOGIN");
        }
        return notify005Svc.notify005(authInfo.getAccountId());
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
}
