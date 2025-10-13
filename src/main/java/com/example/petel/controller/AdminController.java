package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.Admin009Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController extends BaseController {


    /** Admin009 Service */
    private final Admin009Svc admin009Svc;



    /**
     * Admin-009: 刪除賣家（連同其相關的所有物業）
     * @param req Req<Admin009Tranrq>
     * @param errors 驗證錯誤
     * @return Res<Admin009Tranrs>
     * @throws DataNotFoundException 賣家不存在
     * @throws InvalidInputException 輸入驗證錯誤
     */
    @PostMapping("/sellers/delete")
    public Res<Admin009Tranrs> deleteSeller(@Valid @RequestBody Req<Admin009Tranrq> req, Errors errors)
            throws Exception, DataNotFoundException, InvalidInputException {
        handleValidForDto(errors);
        return admin009Svc.deleteSeller(req);
    }
}
