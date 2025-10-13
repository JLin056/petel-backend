package com.example.petel.controller;

import com.example.petel.dto.S3SignUploadReq;
import com.example.petel.dto.S3SignatureRes;
import com.example.petel.service.S3Svc;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
@CrossOrigin("http://localhost:4200")
public class S3Controller {

    private final S3Svc s3Cvc;

    @PostMapping("/sign-upload")
    public S3SignatureRes signUpload(@RequestBody S3SignUploadReq s3SignUploadReq) {
        return s3Cvc.generatePresignedUrl(s3SignUploadReq);
    }
}
