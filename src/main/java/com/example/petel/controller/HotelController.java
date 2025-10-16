
package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.service.HOTEL002Svc;
import com.example.petel.service.HOTEL003Svc;
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
     * HOTEL002 Service
     */
    private final HOTEL002Svc hotel002Svc;

    @PostMapping(value = "/detail")
    public Res<HOTEL002Tranrs<HOTEL002TranrsHotel>> details(@Valid @RequestBody Req<HOTEL002Tranrq> hotel002Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, UpdateFailException {
        handleValidForDto(errors);
        return hotel002Svc.details(hotel002Tranrq);
    }

    /**
     * HOTEL003 Service
     */
    private final HOTEL003Svc hotel003Svc;

    @PostMapping(value = "/rooms")
    public Res<HOTEL003Tranrs<HOTEL003TranrsRoom>> rooms(@Valid @RequestBody Req<HOTEL003Tranrq> hotel003Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return hotel003Svc.rooms(hotel003Tranrq);
    }
}
