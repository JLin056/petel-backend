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
    public Res<Object> book006(@Valid @RequestBody Req<BOOK006Tranrq> requestBody, Errors errors) throws Exception {
        handleValidForDto(errors);
        return book006Svc.book006(requestBody);
    }
}