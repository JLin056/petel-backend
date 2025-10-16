package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.*;
import com.example.petel.service.MERCH001Svc;
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
     * MERCH001 Service
     */
    private final MERCH001Svc merch001Svc;

    @PostMapping(value = "/bookings/list")
    public Res<MERCH001Tranrs<MERCH001TranrsBooking>> list(@Valid @RequestBody Req<MERCH001Tranrq> merch001Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return merch001Svc.list(merch001Tranrq);
    }
}
