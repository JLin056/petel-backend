package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.service.IMG001Svc;
import com.example.petel.service.IMG002Svc;
import com.example.petel.service.IMG003Svc;
import com.example.petel.service.S3Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
@CrossOrigin("http://localhost:4200")
public class S3Controller extends BaseController {

    private final S3Svc s3Svc;
    private final IMG001Svc img001Svc;
    private final IMG002Svc img002Svc;
    private final IMG003Svc img003Svc;

    /**
     * 生成預簽名 URL
     */
    @PostMapping("/sign-upload")
    public S3SignatureRes signUpload(@RequestBody S3SignUploadReq s3SignUploadReq) {
        return s3Svc.generatePresignedUrl(s3SignUploadReq);
    }

    /**
     * 確認圖片已上傳並儲存元資料 (使用 Presigned URL 後呼叫)
     */
    @PostMapping("/create")
    public Res<IMG001Tranrs> confirmImageUpload(@Valid @RequestBody Req<IMG001Tranrq> req, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return img001Svc.uploadImage(req);
    }

    /**
     * 更新圖片 (替換 S3 檔案)
     */
    @PostMapping("/update")
    public Res<IMG002Tranrs> updateImage(@Valid @RequestBody Req<IMG002Tranrq> req, Errors errors)
            throws UpdateFailException, InvalidInputException {
        handleValidForDto(errors);
        return img002Svc.updateImage(req);
    }

    /**
     * 刪除圖片 (刪除 S3 檔案和資料庫記錄)
     */
    @PostMapping("/delete")
    public Res<IMG003Tranrs> deleteImage(@Valid @RequestBody Req<IMG003Tranrq> req, Errors errors)
            throws DeleteFailException, InvalidInputException {
        handleValidForDto(errors);
        return img003Svc.deleteImage(req);
    }

}

