package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
<<<<<<< HEAD
import com.example.petel.service.Admin007Svc;
=======
import com.example.petel.service.ADMIN003Svc;
import com.example.petel.service.ADMIN001Svc;
import com.example.petel.service.ADMIN006Svc;

>>>>>>> dev
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
=======
import java.io.IOException;

>>>>>>> dev
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin("http://localhost:4200")
public class AdminController extends BaseController {

<<<<<<< HEAD
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

=======
    /** Admin001 Service */
    private final ADMIN001Svc admin001Svc;

    /** Admin006 Service */
    private final ADMIN006Svc admin006Svc;
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

    /**
     * Admin-001: 查詢所有旅館列表
     * @param req Req<ADMIN001Tranrq>
     * @param errors 驗證錯誤
     * @return Res<ADMIN001Tranrs>
     * @throws DataNotFoundException 查無資料
     * @throws InvalidInputException 輸入驗證錯誤
     * @throws IOException SQL 檔案讀取錯誤
     */
    @PostMapping("/hotels/queryStore")
    public Res<ADMIN001Tranrs> queryStores(@Valid @RequestBody Req<ADMIN001Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return admin001Svc.queryStores(req);
    }

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
>>>>>>> dev
}
