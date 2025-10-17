package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.entity.RoomsEntity;
import com.example.petel.exception.InsertFailException;
import com.example.petel.model.IdUtil;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PropertyRepository;
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
            log.info("[MERCH-004] 新增成功，新增欄位：{}", merch004Tranrq);

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
