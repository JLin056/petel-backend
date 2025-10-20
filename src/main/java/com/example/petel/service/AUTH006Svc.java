package com.example.petel.service;

import com.example.petel.dto.AUTH006Tranrs;
import com.example.petel.dto.Res;
import com.example.petel.exception.JwtProcessingException;
import com.example.petel.model.jwt.AccountPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface AUTH006Svc {

    Res<AUTH006Tranrs> getInfo(AccountPrincipal authInfo)
            throws JwtProcessingException;
}
