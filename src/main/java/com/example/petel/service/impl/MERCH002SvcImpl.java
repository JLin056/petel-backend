package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.RoomsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.RoomsRepository;
import com.example.petel.service.MERCH002Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH002SvcImpl implements MERCH002Svc {
    /**
     * RoomRepository
     */
    private final RoomsRepository roomRepository;


    @Override
    public Res<MERCH002Tranrs<MERCH002TranrsRoom>> roomList(Req<MERCH002Tranrq> merch002Tranrq) throws DataNotFoundException {
        log.info("-------- [MERCH-002] 查詢單旅館房型 ---------");

        String propertyId = merch002Tranrq.getTranrq().getPropertyId();
        log.info("[MERCH-002] 查詢 propertyId = {}", propertyId);

        List<RoomsEntity> rooms = roomRepository.findByPropertyId(propertyId);
        if (rooms.isEmpty()) {
            log.warn("[MERCH-002] 依據 propertyId 查無資料");
            throw new DataNotFoundException("查無房型資料");
        }

        List<MERCH002TranrsRoom> roomList = new ArrayList<>();
        for (RoomsEntity roomEntity : rooms) {
            MERCH002TranrsRoom room = new MERCH002TranrsRoom();
            room.setId(roomEntity.getId());
            room.setPetTypeId(roomEntity.getPetTypeId());
            room.setName(roomEntity.getName());
            room.setTotalUnits(roomEntity.getTotalUnits());
            room.setBasePrice(roomEntity.getBasePrice());
            room.setInfo(roomEntity.getInfo());
            room.setRoomSize(roomEntity.getRoomSize());
            roomList.add(room);
        }

        log.info("[MERCH-002] 查詢成功，propertyId={}, roomList={}", propertyId, roomList);
        MERCH002Tranrs<MERCH002TranrsRoom> merch002Tranrs = new MERCH002Tranrs<>();
        merch002Tranrs.setRooms(roomList);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), merch002Tranrs);
    }
}