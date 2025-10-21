package com.example.petel.service.impl;

import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.dto.USER004Tranrs;
import com.example.petel.entity.UsersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.USER004Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class USER004SvcImpl implements USER004Svc {

    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** UsersRepository */
    private final AccountsRepository accountsRepo;

    /**
     * 查詢會員資訊
     * @param accountId
     * @return
     * @throws DataNotFoundException
     */
    @Override
    public Res<USER004Tranrs> getUserInfo(String accountId) throws DataNotFoundException {
        log.info("---- [USER-004] 查詢會員資訊 ----");

        UsersEntity usersEntity = usersRepo.findByAccountId(accountId)
                .orElseThrow(() -> new DataNotFoundException("查無會員資訊"));

        String email = accountsRepo.findEmailById(accountId);

        USER004Tranrs tranrs = new USER004Tranrs();
        tranrs.setId(usersEntity.getId());
        tranrs.setAccountId(accountId);
        tranrs.setName(usersEntity.getName());
        tranrs.setPhone(usersEntity.getPhone());
        tranrs.setEmail(email);
        tranrs.setMediaId(usersEntity.getMediaId());

        return new Res<USER004Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    };
}
