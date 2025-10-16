package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.MERCH009Tranrq;
import com.example.petel.dto.MERCH009Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.*;
import com.example.petel.service.MERCH009Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchants")
@CrossOrigin("http://localhost:4200")
public class MerchController extends BaseController {

    /**
     * MERCH009 Service
     */
    private final MERCH009Svc merch009Svc;

    @PostMapping(value = "/sellers/create")
    public Res<MERCH009Tranrs> createSeller(@Valid @RequestBody Req<MERCH009Tranrq> merch009Tranrq, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch009Svc.createSeller(merch009Tranrq);
    }
}
