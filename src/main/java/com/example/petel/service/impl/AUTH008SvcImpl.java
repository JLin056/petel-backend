package com.example.petel.service.impl;

import com.example.petel.dto.AUTH008Tranrs;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.service.AUTH008Svc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AUTH008SvcImpl implements AUTH008Svc {

    @Override
    public Res<AUTH008Tranrs> check(HttpServletRequest request) {
        log.info("---- [AUTH-008] 驗證使用者狀態 ----");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean valid =  auth instanceof UsernamePasswordAuthenticationToken && // 如果是 null，未登入
                auth.isAuthenticated() && // 已驗證
                auth.getPrincipal() != null && // 有使用者資訊
                auth.getAuthorities() != null && // 有權限資訊
                !auth.getAuthorities().isEmpty(); // 檢查真的有值
        log.info("[AUTH-008] 驗證完成，valid ={}", valid);

        AUTH008Tranrs tranrs = new AUTH008Tranrs(valid);
        return new Res<AUTH008Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
