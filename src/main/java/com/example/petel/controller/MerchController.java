package com.example.petel.controller;

import java.io.IOException;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.MERCH001Tranrq;
import com.example.petel.dto.MERCH001Tranrs;
import com.example.petel.dto.MERCH001TranrsBooking;
import com.example.petel.dto.MERCH003Tranrq;
import com.example.petel.dto.MERCH003Tranrs;
import com.example.petel.dto.MERCH003TranrsReview;
import com.example.petel.dto.MERCH004Tranrq;
import com.example.petel.dto.MERCH004Tranrs;
import com.example.petel.dto.MERCH005Tranrq;
import com.example.petel.dto.MERCH005Tranrs;
import com.example.petel.dto.MERCH008Tranrq;
import com.example.petel.dto.MERCH008Tranrs;
import com.example.petel.dto.MERCH009Tranrq;
import com.example.petel.dto.MERCH009Tranrs;
import com.example.petel.dto.MERCH010Tranrq;
import com.example.petel.dto.MERCH010Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.service.MERCH001Svc;
import com.example.petel.service.MERCH003Svc;
import com.example.petel.service.MERCH004Svc;
import com.example.petel.service.MERCH005Svc;
import com.example.petel.service.MERCH008Svc;
import com.example.petel.service.MERCH009Svc;
import com.example.petel.service.MERCH010Svc;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
     * MERCH003 Service
     */
    private final MERCH003Svc merch003Svc;

    /**
     * MERCH004 Service
     */
    private final MERCH004Svc merch004Svc;

    /**
     * MERCH005 Service
     */
    private final MERCH005Svc merch005Svc;

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

    @PostMapping(value = "/hotels/reviews")
    public Res<MERCH003Tranrs<MERCH003TranrsReview>> reviews(@Valid @RequestBody Req<MERCH003Tranrq> merch003Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return merch003Svc.reviews(merch003Tranrq);
    }

    @PostMapping(value = "/rooms/edit")
    public Res<MERCH005Tranrs> edit(@Valid @RequestBody Req<MERCH005Tranrq> merch005Tranrq, Errors errors)
            throws DataNotFoundException, UpdateFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch005Svc.edit(merch005Tranrq);
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
