package com.example.petel.service;

import com.example.petel.dto.Res;
import com.example.petel.dto.AUTH009Tranrs;
import jakarta.servlet.http.HttpServletRequest;

public interface AUTH009Svc {

    Res<AUTH009Tranrs> check(String accountId, String role);
}
