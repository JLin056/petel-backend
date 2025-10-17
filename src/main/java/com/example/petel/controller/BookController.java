package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.BOOK001Svc;
import com.example.petel.service.BOOK002Svc;
import com.example.petel.service.BOOK006Svc;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
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
  
    @PostMapping(value = "/credit")
    public Res<BOOK006Tranrs> book006(@Valid @RequestBody Req<BOOK006Tranrq> requestBody, Errors errors) throws Exception {
        handleValidForDto(errors);
        return book006Svc.book006(requestBody);
    }
}