package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.*;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /**
     * MERCH012 Service
     */
    private final MERCH012Svc merch012Svc;

    /**
     * MERCH013 Service
     */
    private final MERCH013Svc merch013Svc;

    /**
     * MERCH014 Service
     */
    private final MERCH014Svc merch014Svc;

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

    @PostMapping(value = "/rooms/get")
    public Res<MERCH012Tranrs> getRoomInfo(@Valid @RequestBody Req<MERCH012Tranrq> merch012Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException {
        handleValidForDto(errors);
        return merch012Svc.getRoomInfo(merch012Tranrq);
    }

    @PostMapping(value = "/properties/get")
    public Res<MERCH013Tranrs<MERCH013TranrsProperty>> getProperties(@Valid @RequestBody Req<MERCH013Tranrq> merch013Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException {
        handleValidForDto(errors);
        return merch013Svc.getProperties(merch013Tranrq);
    }

    @PostMapping("/bookings/updateStatus")
    public Res<MERCH014Tranrs> updateOrderStatus(@Valid @RequestBody Req<MERCH014Tranrq> rq, Errors errors)
            throws DataNotFoundException, UpdateFailException, InvalidInputException {
        handleValidForDto(errors);
        return merch014Svc.updateStatus(rq);
    }
}

