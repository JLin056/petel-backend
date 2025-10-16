package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.MERCH008Tranrq;
import com.example.petel.dto.MERCH008Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.*;
import com.example.petel.service.MERCH008Svc;
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
     * MERCH008 Service
     */
    private final MERCH008Svc merch008Svc;

    @PostMapping(value = "/hotels/create")
    public Res<MERCH008Tranrs> insert(@Valid @RequestBody Req<MERCH008Tranrq> merch008Tranrq, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch008Svc.insert(merch008Tranrq);
    }
}
