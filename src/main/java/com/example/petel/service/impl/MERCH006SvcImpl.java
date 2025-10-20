package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.DeleteFailException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.RoomsRepository;
import com.example.petel.service.MERCH006Svc;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MERCH006SvcImpl implements MERCH006Svc {

    /**
     * RoomRepository
     */
    private final RoomsRepository roomRepository;

    /**
     * 刪除房型資訊
     *
     * @param merch006Tranrq Req<MERCH006Tranrq> （id)
     * @return Res<MERCH006Tranrs>
     * @throws DataNotFoundException,DeleteFailException
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Res<MERCH006Tranrs> delete(Req<MERCH006Tranrq> merch006Tranrq) throws DataNotFoundException, DeleteFailException {
        log.info("-------- [MERCH-006] 刪除房型 ---------");
        String roomId = merch006Tranrq.getTranrq().getId();
        log.info("[MERCH-006] 查詢 roomId = {}", roomId);

        if (!roomRepository.existsById(roomId)) {
            log.warn("[MERCH-006] 依據 roomId 查無資料");
            throw new DataNotFoundException("查無房型資料");
        }

        try {
            roomRepository.deleteById(roomId);
            log.info("[MERCH-006] 刪除成功，roomId={}", roomId);
        } catch (RuntimeException e) {
            log.error("[MERCH-006] 刪除房型失敗，roomId={}", roomId, e);
            throw new DeleteFailException("刪除房型失敗");
        }
        MERCH006Tranrs merch006Tranrs = new MERCH006Tranrs();

        return new Res(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                merch006Tranrs
        );
    }
}
