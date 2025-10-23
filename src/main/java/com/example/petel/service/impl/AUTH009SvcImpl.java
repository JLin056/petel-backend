package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.repository.SellersRepository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.AUTH009Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AUTH009SvcImpl implements AUTH009Svc {

    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** UsersRepository */
    private final SellersRepository sellersRepo;

    @Override
    public Res<AUTH009Tranrs> check(String accountId, String role) {
        log.info("---- [USER-007] 驗證使用者是否填寫會員或商家資訊 ----");

        boolean filled;
        if ("user".equalsIgnoreCase(role)) {
            filled = usersRepo.existsByAccountId(accountId);
        } else if ("seller".equalsIgnoreCase(role)) {
            filled = sellersRepo.existsByAccountId(accountId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "不支援的角色：" + role);
        }

        log.info("[PROFILE-CHECK] filled={}", filled);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                new AUTH009Tranrs(filled));
    }
}
