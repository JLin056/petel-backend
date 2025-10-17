package com.example.petel.service;

import com.example.petel.dto.AUTH008Tranrs;
import com.example.petel.dto.Res;
import jakarta.servlet.http.HttpServletRequest;

public interface AUTH008Svc {

    /** 驗證使用者狀態 */
    Res<AUTH008Tranrs> check(HttpServletRequest request);
}
