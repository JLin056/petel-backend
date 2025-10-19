package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.USER001Svc;
import com.example.petel.service.USER002Svc;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class UserController extends BaseController {

    /** USER001Svc */
    private final USER001Svc user001Svc;
    /** USER002Svc */
    private final USER002Svc user002Svc;

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
}
