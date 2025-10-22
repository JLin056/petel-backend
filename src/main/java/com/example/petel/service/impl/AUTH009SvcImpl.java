package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.USER007Svc;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class USER007SvcImpl implements USER007Svc {

    /** UsersRepository */
    private final UsersRepository usersRepo;

    @Override
    public Res<USER007Tranrs> check(String accountId) {
        log.info("---- [USER-007] 驗證使用者是否填寫會員資訊 ----");

        boolean existsByAccountId = usersRepo.existsByAccountId(accountId);
        USER007Tranrs tranrs = new USER007Tranrs();
        if (existsByAccountId) {
            

        }

        USER004Tranrs tranrs = new USER004Tranrs();


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean valid =  auth instanceof UsernamePasswordAuthenticationToken && // 如果是 null，未登入
                auth.isAuthenticated() && // 已驗證
                auth.getPrincipal() != null && // 有使用者資訊
                auth.getAuthorities() != null && // 有權限資訊
                !auth.getAuthorities().isEmpty(); // 檢查真的有值
        log.info("[AUTH-008] 驗證完成，valid ={}", valid);


        return new Res<AUTH008Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    }
}
