package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.*;
import com.example.petel.service.*;
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

    /**
     * MERCH004 Service
     */
    private final MERCH004Svc merch004Svc;

    /**
     * MERCH008 Service
     */
    private final MERCH008Svc merch008Svc;

    /**
     * MERCH009 Service
     */
    private final MERCH009Svc merch009Svc;

    /**
     * MERCH010 Service
     */
    private final MERCH010Svc merch010Svc;

    @PostMapping(value = "/bookings/list")
    public Res<MERCH001Tranrs<MERCH001TranrsBooking>> list(@Valid @RequestBody Req<MERCH001Tranrq> merch001Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return merch001Svc.list(merch001Tranrq);
    }

    @PostMapping(value = "/rooms/create")
    public Res<MERCH004Tranrs> create(@Valid @RequestBody Req<MERCH004Tranrq> merch004Tranrq, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch004Svc.create(merch004Tranrq);
    }

    @PostMapping(value = "/hotels/create")
    public Res<MERCH008Tranrs> insert(@Valid @RequestBody Req<MERCH008Tranrq> merch008Tranrq, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch008Svc.insert(merch008Tranrq);
    }

    @PostMapping(value = "/sellers/create")
    public Res<MERCH009Tranrs> createSeller(@Valid @RequestBody Req<MERCH009Tranrq> merch009Tranrq, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch009Svc.createSeller(merch009Tranrq);
    }

    @PostMapping(value = "/sellers/edit")
    public Res<MERCH010Tranrs> editSeller(@Valid @RequestBody Req<MERCH010Tranrq> merch010Tranrq, Errors errors)
            throws UpdateFailException, InvalidInputException, DataNotFoundException {
        handleValidForDto(errors);
        return merch010Svc.editSeller(merch010Tranrq);
    }
}
