package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.ADMIN001Svc;
import com.example.petel.service.ADMIN002Svc;
import com.example.petel.service.ADMIN003Svc;
import com.example.petel.service.ADMIN004Svc;
import com.example.petel.service.ADMIN006Svc;
import com.example.petel.service.ADMIN007Svc;
import com.example.petel.service.ADMIN008Svc;
import com.example.petel.service.Admin009Svc;
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

    /** ADMIN001 Service */
    private final ADMIN001Svc admin001Svc;

    /** ADMIN002 Service */
    private final ADMIN002Svc admin002Svc;

    /** ADMIN003 Service */
    private final ADMIN003Svc admin003Svc;

    /** ADMIN004 Service */
    private final ADMIN004Svc admin004Svc;

    /** ADMIN006 Service */
    private final ADMIN006Svc admin006Svc;

    /** ADMIN007 Service */
    private final ADMIN007Svc admin007Svc;

    /** ADMIN008 Service */
    private final ADMIN008Svc admin008Svc;

    /** ADMIN009 Service */
    private final Admin009Svc admin009Svc;

 

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
     * Admin-002: 查詢賣家列表
     * @param req Req<Admin002Tranrq>
     * @param errors 驗證錯誤
     * @return Res<Admin002Tranrs>
     * @throws DataNotFoundException 查無資料
     * @throws InvalidInputException 輸入驗證錯誤
     * @throws IOException SQL 檔案讀取錯誤
     */
    @PostMapping("/merchant/query")
    public Res<ADMIN002Tranrs> querySellers(@Valid @RequestBody Req<ADMIN002Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return admin002Svc.querySellers(req);
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
            throws DataNotFoundException, InvalidInputException,IOException {
        handleValidForDto(errors);
        return admin007Svc.queryMembers(req);
    }


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
     * Admin-004: 更新訂單備註
     *
     * @param req    Req<ADMIN004Tranrq>
     * @param errors 驗證錯誤
     * @return Res<ADMIN004Tranrs>
     * @throws DataNotFoundException 訂單不存在
     * @throws InvalidInputException 輸入驗證錯誤
     * @throws com.example.petel.exception.UpdateFailException 更新失敗
     */
    @PostMapping("/bookings/edit")
    public Res<ADMIN004Tranrs> updateOrderNote(@Valid @RequestBody Req<ADMIN004Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, com.example.petel.exception.UpdateFailException, IOException {
        handleValidForDto(errors);
        return admin004Svc.updateOrderNote(req);
    }

    /**
     * Admin-008: 刪除使用者（連同其關聯的帳號）
     */
    @PostMapping("/members/delete")
    public Res<ADMIN008Tranrs> deleteUser(
            @Valid @RequestBody Req<ADMIN008Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, com.example.petel.exception.DeleteFailException {
        handleValidForDto(errors);
        return admin008Svc.deleteUser(req);
    }

    /**
     * Admin-009: 刪除賣家（連同其相關的所有物業和帳號）
     * @param req Req<Admin009Tranrq>
     * @param errors 驗證錯誤
     * @return Res<Admin009Tranrs>
     * @throws DataNotFoundException 賣家不存在
     * @throws InvalidInputException 輸入驗證錯誤
     * @throws com.example.petel.exception.DeleteFailException 刪除失敗
     */
    @PostMapping("/merchant/delete")
    public Res<Admin009Tranrs> deleteSeller(
            @Valid @RequestBody Req<Admin009Tranrq> req, Errors errors)
            throws DataNotFoundException, InvalidInputException, com.example.petel.exception.DeleteFailException {
        handleValidForDto(errors);
        return admin009Svc.deleteSeller(req);
    }
}
