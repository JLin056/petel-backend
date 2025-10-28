package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.RoomsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.RoomsRepository;
import com.example.petel.service.MERCH012Svc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH012SvcImpl implements MERCH012Svc {

    /**
     * RoomRepository
     */
    private final RoomsRepository roomRepository;

    /**
     * ObjectMapper
     */
    private final ObjectMapper objectMapper;

    @Override
    public Res<MERCH012Tranrs> getRoomInfo(Req<MERCH012Tranrq> merch012Tranrq) throws DataNotFoundException {
        log.info("-------- [MERCH-012] 查詢單房型資訊 ---------");

        String roomId = merch012Tranrq.getTranrq().getId();
        log.info("[MERCH-012] 查詢 roomId = {}", roomId);

        Optional<RoomsEntity> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            log.warn("[MERCH-012] 依據 roomId 查無資料");
            throw new DataNotFoundException("查無房型資料");
        }

        RoomsEntity roomsEntity = optionalRoom.get();
        MERCH012Tranrs merch012Tranrs = objectMapper.convertValue(roomsEntity, MERCH012Tranrs.class);
        log.info("[MERCH-012] 查詢成功，roomId={}", roomId);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch012Tranrs
        );
    }
}