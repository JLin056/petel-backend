package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.RoomsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.RoomsRepository;
import com.example.petel.service.HOTEL003Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HOTEL003SvcImpl implements HOTEL003Svc {
    /**
     * RoomRepository
     */
    private final RoomsRepository roomRepository;

    /**
     * 查詢單筆旅館房型 * * @param hotel003Tranrq Req<HOTEL003Tranrq> （propertyId) * @return Res<HOTEL003Tranrs> * @throws DataNotFoundException
     */
    @Override
    public Res<HOTEL003Tranrs<HOTEL003TranrsRoom>> rooms(Req<HOTEL003Tranrq> hotel003Tranrq) throws DataNotFoundException {
        log.info("-------- [HOTEL-003] 查詢單旅館房型 ---------");

        String propertyId = hotel003Tranrq.getTranrq().getPropertyId();
        log.info("[HOTEL-003] 查詢 propertyId = {}", propertyId);
        List<RoomsEntity> rooms = roomRepository.findByPropertyId(propertyId);
        if (rooms.isEmpty()) {
            log.warn("[HOTEL-003] 依據 propertyId 查無資料");
            throw new DataNotFoundException("查無房型資料");
        }

        String petTypeId = hotel003Tranrq.getTranrq().getPetTypeId();
        log.info("[HOTEL-003] 查詢 petTypeId = {}", petTypeId);

        List<RoomsEntity> filteredRooms = new ArrayList<>();
        for (RoomsEntity r : rooms) {
            if ("W001".equals(petTypeId) && "W001".equals(r.getPetTypeId())) {
                filteredRooms.add(r);
            } else if (!"W001".equals(petTypeId) && !"W001".equals(r.getPetTypeId())) {
                filteredRooms.add(r);
            }
        }

        if (filteredRooms.isEmpty()) {
            log.warn("[HOTEL-003] 查無對應 petTypeId 的房型");
            throw new DataNotFoundException("查無對應寵物種類的房型");
        }

        List<HOTEL003TranrsRoom> roomList = new ArrayList<>();
        for (RoomsEntity roomEntity : filteredRooms) {
            HOTEL003TranrsRoom room = new HOTEL003TranrsRoom();
            room.setName(roomEntity.getName());
            room.setTotalUnits(roomEntity.getTotalUnits());
            room.setBasePrice(roomEntity.getBasePrice());
            room.setInfo(roomEntity.getInfo());
            room.setRoomSize(roomEntity.getRoomSize());
            roomList.add(room);
        }

        log.info("[HOTEL-003] 查詢成功，propertyId={}, roomList={}", propertyId, roomList);
        HOTEL003Tranrs<HOTEL003TranrsRoom> hotel003Tranrs = new HOTEL003Tranrs<>();
        hotel003Tranrs.setRooms(roomList);
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS), hotel003Tranrs);
    }
}