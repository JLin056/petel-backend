package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.Admin007Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController extends BaseController {

    /** Admin007 Service */
    private final Admin007Svc admin007Svc;

    /**
     * Admin-007: 查詢會員列表
     *
     * @param req    Req<ADMIN007Tranrq>
     * @param errors 驗證錯誤
     * @return Res<ADMIN007Tranrs>
     * @throws DataNotFoundException 查無資料
     * @throws InvalidInputException 輸入驗證錯誤
     */
    @PostMapping("/queryMembers")
    public Res<ADMIN007Tranrs> queryMembers(@Valid @RequestBody Req<ADMIN007Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException {
        handleValidForDto(errors);
        return admin007Svc.queryMembers(req);
    }

}
