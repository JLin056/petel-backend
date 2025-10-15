package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.MERCH004Tranrq;
import com.example.petel.dto.MERCH004Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.MERCH004Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchants")
@CrossOrigin("http://localhost:4200")
public class MerchController extends BaseController {

    /**
     * MERCH004 Service
     */
    private final MERCH004Svc merch004Svc;

    @PostMapping(value = "/rooms/create")
    public Res<MERCH004Tranrs> create(@Valid @RequestBody Req<MERCH004Tranrq> merch004Tranrq, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch004Svc.create(merch004Tranrq);
    }
}
