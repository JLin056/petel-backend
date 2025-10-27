package com.example.petel.service;

import com.example.petel.dto.AUTH010Tranrs;
import com.example.petel.dto.Res;
import com.example.petel.exception.JwtProcessingException;
import jakarta.servlet.http.HttpServletResponse;

public interface AUTH010Svc {

    Res<AUTH010Tranrs> refresh(String refreshToken, HttpServletResponse resp)
            throws JwtProcessingException;
}
