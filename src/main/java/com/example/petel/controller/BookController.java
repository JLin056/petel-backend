package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.*;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
@RequestMapping("/bookings")
public class BookController extends BaseController {

    /** BOOK001Svc */
    private final BOOK001Svc book001Svc;

    /** BOOK002Svc */
    private final BOOK002Svc book002Svc;

    /** BOOK003Svc */
    private final BOOK003Svc book003Svc;

    /** BOOK004Svc */
    private final BOOK004Svc book004Svc;

    /** BOOK005Svc */
    private final BOOK005Svc book005Svc;

    /** BOOK006Svc */
    private final BOOK006Svc book006Svc;

    /** BOOK008Svc */
    private final BOOK008Svc book008Svc;

    /** BOOK009Svc */
    private final BOOK009Svc book009Svc;

    /** BOOK013Svc */
    private final BOOK013Svc book013Svc;

    @PostMapping(value = "/create")
    public Res<BOOK001Tranrs> book001(@AuthenticationPrincipal AccountPrincipal authInfo,
            @Valid @RequestBody Req<BOOK001Tranrq> requestBody, Errors errors) throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return book001Svc.book001(authInfo.getAccountId(), requestBody);
    }

    @PostMapping(value = "/detail")
    public Res<BOOK002Tranrs> book002(@Valid @RequestBody Req<BOOK002Tranrq> requestBody, Errors errors) throws DataNotFoundException, IOException, InvalidInputException {
        handleValidForDto(errors);
        return book002Svc.book002(requestBody);
    }

    @PostMapping(value = "/update")
    public Res<Object> book003(@Valid @RequestBody Req<BOOK003Tranrq> requestBody, Errors errors) throws DataNotFoundException, UpdateFailException, InvalidInputException {
        handleValidForDto(errors);
        return book003Svc.book003(requestBody);
    }

    @PostMapping(value = "/cancel")
    public Res<Object> book004(@Valid @RequestBody Req<BOOK004Tranrq> requestBody, Errors errors) throws DataNotFoundException, DeleteFailException, InvalidInputException {
        handleValidForDto(errors);
        return book004Svc.book004(requestBody);
    }

    @PostMapping(value = "/authorize")
    public Res<BOOK005Tranrs> book005(@Valid @RequestBody Req<BOOK005Tranrq> requestBody, Errors errors) throws Exception {
        handleValidForDto(errors);
        return book005Svc.book005(requestBody);
    }

    @PostMapping(value = "/credit")
    public Res<BOOK006Tranrs> book006(@Valid @RequestBody Req<BOOK006Tranrq> requestBody, Errors errors) throws Exception {
        handleValidForDto(errors);
        return book006Svc.book006(requestBody);
    }

    @PostMapping(value = "/authorize/notify")
    public String book008(@RequestBody BOOK008Tranrq requestBody) throws Exception {
        return book008Svc.book008(requestBody);
    }

    @PostMapping(value = "/credit/notify")
    public String book009(@RequestParam Map<String, Object> requestParam) throws Exception {
        return book009Svc.book009(requestParam);
    }

    @PostMapping(value = "/simulate/notify")
    public Res<Object> book013(@Valid @RequestBody Req<BOOK013Tranrq> requestBody, Errors errors) throws Exception {
        return book013Svc.book013(requestBody);
    }
}