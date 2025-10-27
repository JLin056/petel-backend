package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthController extends BaseController{
    /** AUTH001 Service */
    private final AUTH001Svc auth001Svc;
    /** AUTH002 Service */
    private final AUTH002Svc auth002Svc;
    /** AUTH003 Service */
    private final AUTH003Svc auth003Svc;
    /** AUTH006 Service */
    private final AUTH006Svc auth006Svc;
    /** AUTH008 Service */
    private final AUTH008Svc auth008Svc;
    /** AUTH009 Service */
    private final AUTH009Svc auth009Svc;
    /** AUTH004Svc Service */
    private final AUTH004Svc auth004Svc;
    /** AUTH005Svc Service */
    private final AUTH005Svc auth005Svc;
    /** AUTH010Svc Service */
    private final AUTH010Svc auth010Svc;

    /**
     * 註冊
     * @param req
     * @param errors
     * @return
     * @throws InsertFailException
     * @throws InvalidInputException
     */
    @PostMapping("/register")
    public Res<AUTH001Tranrs> register(@Valid @RequestBody Req<AUTH001Tranrq> req, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return auth001Svc.register(req);
    }

    /**
     * 登入
     * @param req
     * @param resp
     * @param errors
     * @return
     * @throws InvalidInputException
     * @throws JwtProcessingException
     */
    @PostMapping("/login")
    public Res<AUTH002Tranrs> login(@Valid @RequestBody Req<AUTH002Tranrq> req,
                                    HttpServletResponse resp,
                                    Errors errors)
            throws InvalidInputException, JwtProcessingException {
        handleValidForDto(errors);
        return auth002Svc.login(req, resp);
    }

    /**
     * 登出
     * @param resp
     * @return
     */
    @PostMapping("/logout")
    public Res<Object> logout(HttpServletRequest request, HttpServletResponse resp) {
        return auth003Svc.logout(request, resp);
    }

    /**
     *
     */
    @PostMapping("/me")
    public Res<AUTH006Tranrs> getInfo(@AuthenticationPrincipal AccountPrincipal authInfo)
            throws JwtProcessingException{
        return auth006Svc.getInfo(authInfo);
    }

    /**
     * 驗證使用者資訊
     * @param request
     * @return
     */
    @PostMapping("/check")
    public Res<AUTH008Tranrs> check(HttpServletRequest request) {
        return auth008Svc.check(request);
    }

    /**
     * 驗證使用者是否填寫資訊
     * @param authInfo
     * @return
     */
    @PostMapping("/profile/check")
    public Res<AUTH009Tranrs> check(@AuthenticationPrincipal AccountPrincipal authInfo) {
        return auth009Svc.check(authInfo.getAccountId(), authInfo.getRole());
    }

    /**
     * 忘記密碼
     * @param req
     * @return
     */
    @PostMapping("/forgot")
    public Res<AUTH004Tranrs> forgot(@Valid @RequestBody Req<AUTH004Tranrq> req) {
        return auth004Svc.forgotPassword(req);
    }

    /**
     * 重設密碼
     * @param req
     * @param errors
     * @return
     * @throws InvalidInputException
     * @throws UpdateFailException
     * @throws IOException
     */
    @PostMapping("/reset")
    public Res<Object> reset(@Valid @RequestBody Req<AUTH005Tranrq> req, Errors errors)
            throws InvalidInputException, UpdateFailException, IOException {
        handleValidForDto(errors);
        return auth005Svc.resetPassword(req);
    }

    /**
     * 重整 Refresh Token
     * @param refreshToken
     * @param resp
     * @return
     * @throws JwtProcessingException
     */
    @PostMapping("/refresh")
    public Res<AUTH010Tranrs> refresh(
            @CookieValue(name="refresh_token", required = false) String refreshToken,
            HttpServletResponse resp)
        throws JwtProcessingException {
        return auth010Svc.refresh(refreshToken, resp);
    }

}
