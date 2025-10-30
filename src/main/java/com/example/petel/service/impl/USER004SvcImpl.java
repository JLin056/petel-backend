package com.example.petel.service.impl;

import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.dto.USER004Tranrs;
import com.example.petel.entity.MediaBase64Entity;
import com.example.petel.entity.UsersEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.AccountsRepository;
import com.example.petel.repository.MediaBase64Repository;
import com.example.petel.repository.UsersRepository;
import com.example.petel.service.USER004Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class USER004SvcImpl implements USER004Svc {

    /** UsersRepository */
    private final UsersRepository usersRepo;
    /** UsersRepository */
    private final AccountsRepository accountsRepo;
    /** MediaBase64Repository */
    private final MediaBase64Repository mediaBase64Repo;

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

        String mediaId = usersEntity.getMediaId();
        String avatarDataUrl = null;

        if (mediaId != null) {
            Optional<MediaBase64Entity> mediaEntity = mediaBase64Repo.findById(mediaId);
            if (mediaEntity.isPresent()){
                MediaBase64Entity media = mediaEntity.get();
                avatarDataUrl = String.format(
                        "data:%s;base64,%s",
                        media.getMimeType(),
                        media.getBase64Data()
                );
            } else {
                log.warn("[USER-004] 找不到媒體檔案 ID: {}", mediaId);
            }
        }

        USER004Tranrs tranrs = new USER004Tranrs();
        tranrs.setId(usersEntity.getId());
        tranrs.setAccountId(accountId);
        tranrs.setName(usersEntity.getName());
        tranrs.setPhone(usersEntity.getPhone());
        tranrs.setEmail(email);
        tranrs.setMediaId(mediaId);
        tranrs.setMediaBase64(avatarDataUrl);

        return new Res<USER004Tranrs>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                tranrs
        );
    };
}
