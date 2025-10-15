package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.BOOK001Svc;
import com.example.petel.service.BOOK006Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
@RequestMapping("/bookings")
public class BookController extends BaseController {

    /** BOOK001Svc */
    private final BOOK001Svc book001Svc;

    /** BOOK006Svc */
    private final BOOK006Svc book006Svc;

    @PostMapping(value = "/create")
    public Res<BOOK001Tranrs> book001(@Valid @RequestBody Req<BOOK001Tranrq> requestBody, Errors errors) throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return book001Svc.book001(requestBody);
    }

    @PostMapping(value = "/credit")
    public Res<BOOK006Tranrs> book006(@Valid @RequestBody Req<BOOK006Tranrq> requestBody, Errors errors) throws Exception {
        handleValidForDto(errors);
        return book006Svc.book006(requestBody);
    }
}