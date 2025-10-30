package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.RoomImageEntity;
import com.example.petel.entity.RoomsEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.RoomImageRepository;
import com.example.petel.repository.RoomsRepository;
import com.example.petel.service.MERCH004Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH004SvcImpl implements MERCH004Svc {

    /**
     * RoomsRepository
     */
    private final RoomsRepository roomsRepository;

    /**
     * RoomImageRepository
     */
    private final RoomImageRepository roomImageRepository;

    /**
     * 新增房型資訊
     *
     * @param requestBody Req<MERCH004Tranrq>
     * @return Res<MERCH004Tranrs>
     * @throws InsertFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH004Tranrs> create(Req<MERCH004Tranrq> requestBody) throws InsertFailException {

        log.info("-------- [MERCH-004] 新增房型 ---------");
        MERCH004Tranrq merch004Tranrq = requestBody.getTranrq();

        String newRoomId = IdUtil.generateTableId("R", roomsRepository.findMaxId());
        log.info("[MERCH-004] 生成房型ID:{}", newRoomId);

        try {
            // 1. 新增房型資料到 PETEL_ROOMS
            RoomsEntity roomsEntity = new RoomsEntity();
            roomsEntity.setId(newRoomId);
            roomsEntity.setPropertyId(merch004Tranrq.getPropertyId());
            roomsEntity.setName(merch004Tranrq.getName());
            roomsEntity.setTotalUnits(merch004Tranrq.getTotalUnits());
            roomsEntity.setBasePrice(merch004Tranrq.getBasePrice());
            roomsEntity.setPetTypeId(merch004Tranrq.getPetTypeId());
            roomsEntity.setInfo(merch004Tranrq.getInfo());
            roomsEntity.setRoomSize(merch004Tranrq.getRoomSize());

            roomsRepository.save(roomsEntity);
            log.info("[MERCH-004] 新增房型成功，roomId={}", newRoomId);

            // 2. 新增房型圖片關聯資料到 PETEL_ROOM_IMAGE
            if (merch004Tranrq.getRoomImages() != null && !merch004Tranrq.getRoomImages().isEmpty()) {
                for (MERCH004TranrqRoomImage roomImage : merch004Tranrq.getRoomImages()) {
                    RoomImageEntity roomImageEntity = new RoomImageEntity();
                    roomImageEntity.setRoomId(newRoomId);
                    roomImageEntity.setMediaId(roomImage.getMediaId());
                    roomImageEntity.setSortOrder(roomImage.getSortOrder());

                    roomImageRepository.save(roomImageEntity);
                    log.info("[MERCH-004] 新增房型圖片關聯：roomId={}, mediaId={}, sortOrder={}",
                            newRoomId, roomImage.getMediaId(), roomImage.getSortOrder());
                }
                log.info("[MERCH-004] 共新增 {} 張房型圖片", merch004Tranrq.getRoomImages().size());
            } else {
                log.info("[MERCH-004] 未提供房型圖片");
            }

        } catch (Exception e) {
            log.error("[MERCH-004] 新增房型失敗", e);
            throw new InsertFailException("新增房型失敗：" + e.getMessage());
        }

        MERCH004Tranrs merch004Tranrs = new MERCH004Tranrs();
        return new Res(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch004Tranrs
        );
    }
}
