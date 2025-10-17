package com.example.petel.controller;

import java.io.IOException;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.HOTEL002Tranrq;
import com.example.petel.dto.HOTEL002Tranrs;
import com.example.petel.dto.HOTEL002TranrsHotel;
import com.example.petel.dto.HOTEL003Tranrq;
import com.example.petel.dto.HOTEL003Tranrs;
import com.example.petel.dto.HOTEL003TranrsRoom;
import com.example.petel.dto.HOTEL004Tranrq;
import com.example.petel.dto.HOTEL004Tranrs;
import com.example.petel.dto.HOTEL004TranrsFacility;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.service.HOTEL002Svc;
import com.example.petel.service.HOTEL003Svc;
import com.example.petel.service.HOTEL004Svc;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
@CrossOrigin("http://localhost:4200")
public class HotelController extends BaseController {

    /**
     * HOTEL002 Service
     */
    private final HOTEL002Svc hotel002Svc;

    /**
     * HOTEL003 Service
     */
    private final HOTEL003Svc hotel003Svc;

    /**
     * HOTEL004 Service
     */
    private final HOTEL004Svc hotel004Svc;

    @PostMapping(value = "/detail")
    public Res<HOTEL002Tranrs<HOTEL002TranrsHotel>> details(@Valid @RequestBody Req<HOTEL002Tranrq> hotel002Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, UpdateFailException {
        handleValidForDto(errors);
        return hotel002Svc.details(hotel002Tranrq);
    }

    @PostMapping(value = "/rooms")
    public Res<HOTEL003Tranrs<HOTEL003TranrsRoom>> rooms(@Valid @RequestBody Req<HOTEL003Tranrq> hotel003Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return hotel003Svc.rooms(hotel003Tranrq);
    }

    @PostMapping(value = "/facilities")
    public Res<HOTEL004Tranrs<HOTEL004TranrsFacility>> facilities(@Valid
            @RequestBody Req<HOTEL004Tranrq> hotel004Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return hotel004Svc.facilities(hotel004Tranrq);
    }
}
