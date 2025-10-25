package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.MEDIA001Tranrq;
import com.example.petel.dto.MEDIA001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.MEDIA001Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medias")
@CrossOrigin("http://localhost:4200")
public class MediaController extends BaseController {

    /** MEDIA001 Service */
    private final MEDIA001Svc media001Svc;

    /**
     * Base64 圖片上傳
     * @param req Base64 圖片上傳請求
     * @param errors 驗證錯誤
     * @return 上傳結果
     * @throws InsertFailException 插入失敗異常
     * @throws InvalidInputException 輸入驗證異常
     */
    @PostMapping("/upload/base64")
    public Res<MEDIA001Tranrs> uploadBase64(@Valid @RequestBody Req<MEDIA001Tranrq> req, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return media001Svc.uploadBase64Media(req);
    }
}
