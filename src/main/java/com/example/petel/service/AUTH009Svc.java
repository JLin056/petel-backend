package com.example.petel.service;

import com.example.petel.dto.Res;
import com.example.petel.dto.USER007Tranrs;
import jakarta.servlet.http.HttpServletRequest;

public interface USER007Svc {

    Res<USER007Tranrs> check(HttpServletRequest request);
}
