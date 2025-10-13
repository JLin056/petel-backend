package com.example.petel.service;

import com.example.petel.dto.S3SignUploadReq;
import com.example.petel.dto.S3SignatureRes;

public interface S3Svc {
    public S3SignatureRes generatePresignedUrl (S3SignUploadReq s3SignUploadReq);
}
