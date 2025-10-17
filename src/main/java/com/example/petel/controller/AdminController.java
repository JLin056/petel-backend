package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
<<<<<<< HEAD
import com.example.petel.service.ADMIN003Svc;
=======
import com.example.petel.service.Admin001Svc;
>>>>>>> dev
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

<<<<<<< HEAD
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
=======
    /** Admin001 Service */
    private final Admin001Svc admin001Svc;



    /**
     * Admin-001: 查詢所有旅館列表
     * @param req Req<Admin001Tranrq>
     * @param errors 驗證錯誤
     * @return Res<Admin001Tranrs>
     * @throws DataNotFoundException 查無資料
     * @throws InvalidInputException 輸入驗證錯誤
     * @throws IOException SQL 檔案讀取錯誤
     */
    @PostMapping("/hotels/queryStore")
    public Res<Admin001Tranrs> queryStores(@Valid @RequestBody Req<Admin001Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return admin001Svc.queryStores(req);
    }

>>>>>>> dev
}
