package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;

import com.example.petel.service.Admin006Svc;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController extends BaseController {


    /** Admin006 Service */
    private final Admin006Svc admin006Svc;




    /**
     * Admin-006: 刪除旅館
     * @param req Req<ADMIN006Tranrq>
     * @param errors 驗證錯誤
     * @return Res<ADMIN006Tranrs>
     * @throws DataNotFoundException 旅館不存在
     * @throws InvalidInputException 輸入驗證錯誤
     * @throws com.example.petel.exception.DeleteFailException 刪除失敗
     */
    @PostMapping("/hotels/delete")
    public Res<ADMIN006Tranrs> deleteHotel(@Valid @RequestBody Req<ADMIN006Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, com.example.petel.exception.DeleteFailException {
        handleValidForDto(errors);
        return admin006Svc.deleteHotel(req);
    }

}
