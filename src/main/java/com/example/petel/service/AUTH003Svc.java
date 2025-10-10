package com.example.petel.service;

import com.example.petel.dto.Res;
import jakarta.servlet.http.HttpServletResponse;

public interface AUTH003Svc {

    Res<Object> logout(HttpServletResponse resp);
}
