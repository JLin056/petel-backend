package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.MERCH006Tranrq;
import com.example.petel.dto.MERCH006Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.*;
import com.example.petel.service.MERCH006Svc;
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
     * MERCH006 Service
     */
    private final MERCH006Svc merch006Svc;

    @PostMapping(value = "/rooms/delete")
    public Res<MERCH006Tranrs> delete(@Valid @RequestBody Req<MERCH006Tranrq> merch006Tranrq, Errors errors)
            throws DataNotFoundException, DeleteFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch006Svc.delete(merch006Tranrq);
    }
}
