package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.Admin008Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController extends BaseController {

    /** Admin008 Service */
    private final Admin008Svc admin008Svc;

    /**
     * Admin-008: 刪除使用者（連同其關聯的帳號）
     * @param req Req<Admin008Tranrq>
     * @param errors 驗證錯誤
     * @return Res<Admin008Tranrs>
     * @throws DataNotFoundException 使用者不存在
     * @throws InvalidInputException 輸入驗證錯誤
     * @throws com.example.petel.exception.DeleteFailException 刪除失敗
     */
    @PostMapping("/members/delete")
    public Res<Admin008Tranrs> deleteUser(@Valid @RequestBody Req<Admin008Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, com.example.petel.exception.DeleteFailException {
        handleValidForDto(errors);
        return admin008Svc.deleteUser(req);
    }


}
