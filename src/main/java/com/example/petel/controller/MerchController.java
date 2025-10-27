package com.example.petel.controller;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.petel.dto.MERCH002Tranrq;
import com.example.petel.dto.MERCH002Tranrs;
import com.example.petel.dto.MERCH002TranrsRoom;
import com.example.petel.dto.MERCH003Tranrq;
import com.example.petel.dto.MERCH003Tranrs;
import com.example.petel.dto.MERCH003TranrsReview;
import com.example.petel.dto.MERCH004Tranrq;
import com.example.petel.dto.MERCH004Tranrs;
import com.example.petel.dto.MERCH005Tranrq;
import com.example.petel.dto.MERCH005Tranrs;
import com.example.petel.dto.MERCH006Tranrq;
import com.example.petel.dto.MERCH006Tranrs;
import com.example.petel.dto.MERCH007Tranrq;
import com.example.petel.dto.MERCH007Tranrs;
import com.example.petel.dto.MERCH008Tranrq;
import com.example.petel.dto.MERCH008Tranrs;
import com.example.petel.dto.MERCH009Tranrq;
import com.example.petel.dto.MERCH009Tranrs;
import com.example.petel.dto.MERCH010Tranrq;
import com.example.petel.dto.MERCH010Tranrs;
import com.example.petel.dto.MERCH011Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.MERCH001Svc;
import com.example.petel.service.MERCH002Svc;
import com.example.petel.service.MERCH003Svc;
import com.example.petel.service.MERCH004Svc;
import com.example.petel.service.MERCH005Svc;
import com.example.petel.service.MERCH006Svc;
import com.example.petel.service.MERCH007Svc;
import com.example.petel.service.MERCH008Svc;
import com.example.petel.service.MERCH009Svc;
import com.example.petel.service.MERCH010Svc;
import com.example.petel.service.MERCH011Svc;
import com.fasterxml.jackson.databind.JsonMappingException;

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
     * MERCH002 Service
     */
    private final MERCH002Svc merch002Svc;

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
     * MERCH006 Service
     */
    private final MERCH006Svc merch006Svc;

    /**
     * MERCH007 Service
     */
    private final MERCH007Svc merch007Svc;

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

    /**
     * MERCH011 Service
     */
    private final MERCH011Svc merch011Svc;

    @PostMapping(value = "/bookings/list")
    public Res<MERCH001Tranrs<MERCH001TranrsBooking>> list(@Valid @RequestBody Req<MERCH001Tranrq> merch001Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return merch001Svc.list(merch001Tranrq);
    }

    @PostMapping(value = "/rooms/list")
    public Res<MERCH002Tranrs<MERCH002TranrsRoom>> roomList(@Valid @RequestBody Req<MERCH002Tranrq> merch002Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException {
        handleValidForDto(errors);
        return merch002Svc.roomList(merch002Tranrq);
    }

    @PostMapping(value = "/hotels/reviews")
    public Res<MERCH003Tranrs<MERCH003TranrsReview>> reviews(@Valid @RequestBody Req<MERCH003Tranrq> merch003Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return merch003Svc.reviews(merch003Tranrq);
    }

    @PostMapping(value = "/rooms/create")
    public Res<MERCH004Tranrs> create(@Valid @RequestBody Req<MERCH004Tranrq> merch004Tranrq, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch004Svc.create(merch004Tranrq);
    }

    @PostMapping(value = "/rooms/edit")
    public Res<MERCH005Tranrs> edit(@Valid @RequestBody Req<MERCH005Tranrq> merch005Tranrq, Errors errors)
            throws DataNotFoundException, UpdateFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch005Svc.edit(merch005Tranrq);
    }

    @PostMapping(value = "/rooms/delete")
    public Res<MERCH006Tranrs> delete(@Valid @RequestBody Req<MERCH006Tranrq> merch006Tranrq, Errors errors)
            throws DataNotFoundException, DeleteFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch006Svc.delete(merch006Tranrq);
    }

    @PostMapping(value = "/hotels/edit")
    public Res<MERCH007Tranrs> update(@Valid @RequestBody Req<MERCH007Tranrq> merch007Tranrq, Errors errors)
            throws DataNotFoundException, UpdateFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch007Svc.update(merch007Tranrq);
    }

    @PostMapping(value = "/hotels/create")
    public Res<MERCH008Tranrs> insert(@Valid @RequestBody Req<MERCH008Tranrq> merch008Tranrq, Errors errors)
            throws DataNotFoundException, InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch008Svc.insert(merch008Tranrq);
    }

    @PostMapping(value = "/sellers/create")
    public Res<MERCH009Tranrs> createSeller(@AuthenticationPrincipal AccountPrincipal authInfo,
                                            @Valid @RequestBody Req<MERCH009Tranrq> merch009Tranrq, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch009Svc.createSeller(authInfo.getAccountId(), merch009Tranrq);
    }

    @PostMapping(value = "/sellers/edit")
    public Res<MERCH010Tranrs> editSeller(@AuthenticationPrincipal AccountPrincipal authInfo,
                                          @Valid @RequestBody Req<MERCH010Tranrq> merch010Tranrq, Errors errors)
            throws UpdateFailException, JsonMappingException, InvalidInputException, DataNotFoundException {
        handleValidForDto(errors);
        return merch010Svc.editSeller(authInfo.getAccountId(), merch010Tranrq);
    }

    @PostMapping(value = "/sellers/get")
    public Res<MERCH011Tranrs> getSellerInfo(@AuthenticationPrincipal AccountPrincipal authInfo)
            throws DataNotFoundException {
        return merch011Svc.getSellerInfo(authInfo.getAccountId());
    }
}

