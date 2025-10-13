package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.*;
import com.example.petel.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    /** BOOK007Svc */
    private final BOOK007Svc book007Svc;

    /** BOOK008Svc */
    private final BOOK008Svc book008Svc;

    /** BOOK009Svc */
    private final BOOK009Svc book009Svc;

    /** BOOK010Svc */
    private final BOOK010Svc book010Svc;

    /** BOOK011Svc */
    private final BOOK011Svc book011Svc;

    /** BOOK012Svc */
    private final BOOK012Svc book012Svc;

    /** BOOK013Svc */
    private final BOOK013Svc book013Svc;

    @PostMapping(value = "/create")
    public Res<BOOK001Tranrs> book001(@Valid @RequestBody Req<BOOK001Tranrq> requestBody, Errors errors) throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return book001Svc.book001(requestBody);
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

    @PostMapping(value = "/payments")
    public Res<Object> book005(@Valid @RequestBody Req<BOOK005Tranrq> requestBody, Errors errors) throws Exception {
        handleValidForDto(errors);
        return book005Svc.book005(requestBody);
    }

    @PostMapping(value = "/refunds")
    public Res<Object> book006(@Valid @RequestBody Req<BOOK006Tranrq> requestBody, Errors errors) throws DataNotFoundException, RefundFailException, InvalidInputException {
        handleValidForDto(errors);
        return book006Svc.book006(requestBody);
    }

    @PostMapping(value = "/authorize")
    public Res<BOOK007Tranrs> book007(@Valid @RequestBody Req<BOOK007Tranrq> requestBody, Errors errors) throws Exception {
        handleValidForDto(errors);
        return book007Svc.book007(requestBody);
    }

    @PostMapping(value = "/credit")
    public Res<BOOK008Tranrs> book008(@Valid @RequestBody Req<BOOK008Tranrq> requestBody, Errors errors) throws Exception {
        handleValidForDto(errors);
        return book008Svc.book008(requestBody);
    }

    @PostMapping(value = "/atm")
    public Res<BOOK009Tranrs> book009(@Valid @RequestBody Req<BOOK009Tranrq> requestBody, Errors errors) throws Exception {
        handleValidForDto(errors);
        return book009Svc.book009(requestBody);
    }

    @PostMapping(value = "/authorize/notify")
    public String book010(@RequestBody BOOK010Tranrq requestBody) throws Exception {
        return book010Svc.book010(requestBody);
    }

    @PostMapping(value = "/credit/notify")
    public String book011(@RequestParam Map<String, Object> requestParam) throws Exception {
        return book011Svc.book011(requestParam);
    }

    @PostMapping(value = "/atm/notify")
    public String book012(@RequestParam Map<String, Object> requestParam) throws Exception {
        return book012Svc.book012(requestParam);
    }

    @PostMapping(value = "/v-account/notify")
    public String book013(@RequestParam Map<String, String> requestParam) throws Exception {
        return book013Svc.book013(requestParam);
    }
}