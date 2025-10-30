package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.RoomImageEntity;
import com.example.petel.entity.RoomsEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.RoomImageRepository;
import com.example.petel.repository.RoomsRepository;
import com.example.petel.service.MERCH005Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH005SvcImpl implements MERCH005Svc {

    /**
     * RoomsRepository
     */
    private final RoomsRepository roomsRepository;

    /**
     * RoomImageRepository
     */
    private final RoomImageRepository roomImageRepository;

    /**
     * 修改房型資訊
     *
     * @param requestBody Req<MERCH005Tranrq> （id)
     * @return Res<MERCH005Tranrs>
     * @throws DataNotFoundException,UpdateFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH005Tranrs> edit(Req<MERCH005Tranrq> requestBody) throws DataNotFoundException, UpdateFailException {
        log.info("-------- [MERCH-005] 修改房型 ---------");

        MERCH005Tranrq merch005Tranrq = requestBody.getTranrq();
        String roomId = merch005Tranrq.getId();
        log.info("[MERCH-005] 查詢 roomId = {}", roomId);

        Optional<RoomsEntity> optionals = roomsRepository.findById(roomId);
        if (optionals.isEmpty()) {
            log.warn("[MERCH-005] 依據 roomId 查無資料");
            throw new DataNotFoundException("查無房型資料");
        }

        RoomsEntity existingRoom = optionals.get();

        try {
            existingRoom.setName(merch005Tranrq.getName());
            existingRoom.setTotalUnits(merch005Tranrq.getTotalUnits());
            existingRoom.setBasePrice(merch005Tranrq.getBasePrice());
            existingRoom.setPetTypeId(merch005Tranrq.getPetTypeId());
            existingRoom.setInfo(merch005Tranrq.getInfo());
            existingRoom.setRoomSize(merch005Tranrq.getRoomSize());

            roomsRepository.save(existingRoom);
            log.info("[MERCH-005] 更新成功，roomId={}，更新欄位：{}", roomId, merch005Tranrq);

            if (merch005Tranrq.getRoomImages() != null && !merch005Tranrq.getRoomImages().isEmpty()) {
                // 先刪除舊有圖片
                roomImageRepository.deleteByRoomId(roomId);
                log.info("[MERCH-005] 已刪除舊有房型圖片：roomId={}", roomId);

                // 再新增新圖片
                for (MERCH004TranrqRoomImage roomImage : merch005Tranrq.getRoomImages()) {
                    RoomImageEntity roomImageEntity = new RoomImageEntity();
                    roomImageEntity.setRoomId(roomId);
                    roomImageEntity.setMediaId(roomImage.getMediaId());
                    roomImageEntity.setSortOrder(roomImage.getSortOrder());
                    roomImageRepository.save(roomImageEntity);
                }
                log.info("[MERCH-005] 已新增 {} 張房型圖片", merch005Tranrq.getRoomImages().size());
            }

        } catch (Exception e) {
            log.error("[MERCH-005] 更新房型失敗", e);
            throw new UpdateFailException("更新房型失敗");
        }

        MERCH005Tranrs merch005Tranrs = new MERCH005Tranrs();

        return new Res(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch005Tranrs
        );
    }
}
