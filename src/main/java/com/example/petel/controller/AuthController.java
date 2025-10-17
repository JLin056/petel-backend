package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.service.AUTH001Svc;
import com.example.petel.service.AUTH002Svc;
import com.example.petel.service.AUTH003Svc;
import com.example.petel.service.AUTH008Svc;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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
    /** AUTH008 Service */
    private final AUTH008Svc auth008Svc;

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

    @PostMapping("/check")
    public Res<AUTH008Tranrs> check(HttpServletRequest request) {
        return auth008Svc.check(request);
    }
}
