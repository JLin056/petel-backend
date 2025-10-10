package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.HOTEL005Tranrq;
import com.example.petel.dto.HOTEL005Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.HOTEL005Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
@CrossOrigin("http://localhost:4200")
public class HotelController extends BaseController {

    /** HOTELQ005 Service */
    private final HOTEL005Svc hotel005Svc;

    @PostMapping(value = "/policies")
    public Res<HOTEL005Tranrs> policies(@Valid @RequestBody Req<HOTEL005Tranrq> hotel005Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException {
        handleValidForDto(errors);
        return hotel005Svc.policies(hotel005Tranrq);
    }
}
