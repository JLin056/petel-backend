package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.ADMIN003Svc;
import com.example.petel.service.Admin001Svc;
import com.example.petel.service.Admin006Svc;
import com.example.petel.service.Admin008Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController extends BaseController {

    /** Admin001 Service */
    private final Admin001Svc admin001Svc;

    /** Admin006 Service */
    private final Admin006Svc admin006Svc;

    /** ADMIN003 Service */
    private final ADMIN003Svc admin003Svc;

    /** Admin008 Service */
    private final Admin008Svc admin008Svc;

    /**
     * Admin-003: 查詢訂單列表
     */
    @PostMapping("/bookings/list")
    public Res<ADMIN003Tranrs> queryOrders(
            @Valid @RequestBody Req<ADMIN003Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return admin003Svc.queryOrders(req);
    }

    /**
     * Admin-001: 查詢所有旅館列表
     */
    @PostMapping("/hotels/queryStore")
    public Res<Admin001Tranrs> queryStores(
            @Valid @RequestBody Req<Admin001Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return admin001Svc.queryStores(req);
    }

    /**
     * Admin-006: 刪除旅館
     */
    @PostMapping("/hotels/delete")
    public Res<ADMIN006Tranrs> deleteHotel(
            @Valid @RequestBody Req<ADMIN006Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, com.example.petel.exception.DeleteFailException {
        handleValidForDto(errors);
        return admin006Svc.deleteHotel(req);
    }

    /**
     * Admin-008: 刪除使用者（連同其關聯的帳號）
     */
    @PostMapping("/members/delete")
    public Res<Admin008Tranrs> deleteUser(
            @Valid @RequestBody Req<Admin008Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, com.example.petel.exception.DeleteFailException {
        handleValidForDto(errors);
        return admin008Svc.deleteUser(req);
    }
}
