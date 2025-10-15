package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.MERCH005Tranrq;
import com.example.petel.dto.MERCH005Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.*;
import com.example.petel.service.MERCH005Svc;
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
     * MERCH005 Service
     */
    private final MERCH005Svc merch005Svc;

    @PostMapping(value = "/rooms/edit")
    public Res<MERCH005Tranrs> edit(@Valid @RequestBody Req<MERCH005Tranrq> merch005Tranrq, Errors errors)
            throws DataNotFoundException, UpdateFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch005Svc.edit(merch005Tranrq);
    }
}
