package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.ADMIN002Tranrq;
import com.example.petel.dto.ADMIN002Tranrs;
import com.example.petel.dto.Admin002Tranrq;
import com.example.petel.dto.Admin002Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.Admin002Svc;
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

    /** ADMIN-002 Service */
    private final Admin002Svc admin002Svc;

    /**
     * ADMIN-002: 查詢賣家列表
     * @param admin002Tranrq 請求參數
     * @param errors 驗證錯誤
     * @return 賣家列表
     * @throws DataNotFoundException 查無資料
     * @throws InvalidInputException 輸入驗證錯誤
     * @throws IOException SQL 檔案讀取錯誤
     */
    @PostMapping(value = "/merchant/query")
    public Res<Admin002Tranrs> querySellers(@Valid @RequestBody Req<Admin002Tranrq> admin002Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return admin002Svc.querySellers(admin002Tranrq);
    }
}
