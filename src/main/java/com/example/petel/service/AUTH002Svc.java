package com.example.petel.service;

import com.example.petel.dto.AUTH002Tranrq;
import com.example.petel.dto.AUTH002Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.exception.JwtProcessingException;
import jakarta.servlet.http.HttpServletResponse;

public interface AUTH002Svc {

    Res<AUTH002Tranrs> login(Req<AUTH002Tranrq> req, HttpServletResponse resp)
            throws InvalidInputException, JwtProcessingException;

}
