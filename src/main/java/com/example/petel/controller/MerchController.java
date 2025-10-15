package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.MERCH007Tranrq;
import com.example.petel.dto.MERCH007Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.*;
import com.example.petel.service.MERCH007Svc;
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
     * MERCH007 Service
     */
    private final MERCH007Svc merch007Svc;

    @PostMapping(value = "/hotels/edit")
    public Res<MERCH007Tranrs> update(@Valid @RequestBody Req<MERCH007Tranrq> merch007Tranrq, Errors errors)
            throws DataNotFoundException, UpdateFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch007Svc.update(merch007Tranrq);
    }
}
