package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.USER001Svc;
import com.example.petel.service.USER002Svc;
import com.example.petel.service.USER004Svc;
import com.example.petel.service.USER006Svc;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class UserController extends BaseController {

    /** USER001Svc */
    private final USER001Svc user001Svc;
    /** USER002Svc */
    private final USER002Svc user002Svc;
    /** USER002Svc */
    private final USER004Svc user004Svc;
    /** USER006Svc */
    private final USER006Svc user006Svc;

    /**
     * 新增會員資訊
     * @param authInfo
     * @param req
     * @param errors
     * @return
     * @throws InsertFailException
     * @throws InvalidInputException
     */
    @PostMapping("/create")
    public Res<USER001Tranrs> createUser(@AuthenticationPrincipal AccountPrincipal authInfo,
            @Valid @RequestBody Req<USER001Tranrq> req, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return user001Svc.createUser(authInfo.getAccountId(), req);
    }

    /**
     * 更新會怨資訊
     * @param authInfo
     * @param req
     * @param errors
     * @return
     * @throws InvalidInputException
     * @throws DataNotFoundException
     */
    @PostMapping("/update")
    public Res<USER002Tranrs> updateUser(@AuthenticationPrincipal AccountPrincipal authInfo,
                                         @Valid @RequestBody Req<USER002Tranrq> req,
                                         Errors errors)
            throws DataNotFoundException, JsonMappingException, InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return user002Svc.updateUser(authInfo.getAccountId(), req);
    }

    /**
     * 取得會員資訊
     * @param authInfo
     * @return
     * @throws DataNotFoundException
     */
    @PostMapping("/get")
    public Res<USER004Tranrs> getUser(@AuthenticationPrincipal AccountPrincipal authInfo)
            throws DataNotFoundException {
        return user004Svc.getUserInfo(authInfo.getAccountId());
    }

    /**
     * 取得歷史訂單紀錄列表
     * @param authInfo
     * @param req
     * @param errors
     * @return
     * @throws InvalidInputException
     * @throws DataNotFoundException
     * @throws IOException
     */
    @PostMapping("/bookings/get")
    public Res<USER006Tranrs> getBookingList(@AuthenticationPrincipal AccountPrincipal authInfo,
                                             @RequestBody Req<USER006Tranrq> req,
                                             Errors errors)
            throws InvalidInputException, DataNotFoundException, IOException {
        handleValidForDto(errors);
        return user006Svc.getBookingList(authInfo.getAccountId(), req);
    }

}
