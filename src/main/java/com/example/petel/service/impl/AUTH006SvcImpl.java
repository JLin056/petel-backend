package com.example.petel.service.impl;

import com.example.petel.dto.AUTH006Tranrs;
import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.service.AUTH006Svc;
import com.example.petel.service.AccountQuerySvc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AUTH006SvcImpl implements AUTH006Svc {

    /** AccountsRepository */
    private final AccountsRepository accountRepo;
    /** AccountQuerySvc */
    private final AccountQuerySvc accountQuerySvc;

    /**
     * 取得使用者資訊
     * @param authInfo
     * @return Res<AUTH006Tranrs>
     * @throws JwtProcessingException
     */
    @Override
    public Res<AUTH006Tranrs> getInfo(AccountPrincipal authInfo)
            throws JwtProcessingException {
        if (authInfo == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String accountId = authInfo.getAccountId();
        String role = authInfo.getRole();

        String mainId = accountQuerySvc.getIdByRoleAndAccountId(
                role, accountId
        );

        String email = accountRepo.findEmailById(accountId);

        AUTH006Tranrs tranrs = new AUTH006Tranrs();
        tranrs.setAccountId(accountId);
        tranrs.setEmail(email);
        tranrs.setRole(role);
        tranrs.setMainId(mainId);


        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), tranrs);
    }
}
