package com.example.petel.service.impl;

import com.example.petel.repository.AccountsRepository;
import com.example.petel.repository.SellersRepository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.AccountQuerySvc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountQuerySvcImpl implements AccountQuerySvc{

    /** UsersRepository */
    private final UsersRepository usersRepository;
    /** SellersRepository */
    private final SellersRepository sellersRepository;

    @Override
    public String getIdByRoleAndAccountId(String role, String accountId) {
        return switch (role.toLowerCase()) {
            case "user" -> usersRepository.findIdByAccountId(accountId);
            case "seller" -> sellersRepository.findIdByAccountId(accountId);
            default -> null;
        };
    }



}
