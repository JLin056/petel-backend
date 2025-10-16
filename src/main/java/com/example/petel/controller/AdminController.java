package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.ADMIN003Svc;
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

    /** ADMIN003 Service */
    private final ADMIN003Svc admin003Svc;

    /**
     * Admin-003: 查詢訂單列表
     *
     * @param req    Req<ADMIN003Tranrq>
     * @param errors 驗證錯誤
     * @return Res<ADMIN003Tranrs>
     * @throws DataNotFoundException 查無資料
     * @throws InvalidInputException 輸入驗證錯誤
     */
    @PostMapping("/bookings/list")
    public Res<ADMIN003Tranrs> queryOrders(@Valid @RequestBody Req<ADMIN003Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return admin003Svc.queryOrders(req);
    }
}
