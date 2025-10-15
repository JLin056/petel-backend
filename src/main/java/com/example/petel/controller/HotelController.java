package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.service.HOTEL004Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
@CrossOrigin("http://localhost:4200")
public class HotelController extends BaseController {

    /**
     * HOTEL004 Service
     */
    private final HOTEL004Svc hotel004Svc;

    @PostMapping(value = "/facilities")
    public Res<HOTEL004Tranrs<HOTEL004TranrsFacility>> facilities(@Valid @RequestBody Req<HOTEL004Tranrq> hotel004Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return hotel004Svc.facilities(hotel004Tranrq);
    }
}
