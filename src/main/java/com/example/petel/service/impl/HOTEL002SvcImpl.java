package com.example.petel.service.impl;

import com.example.petel.dto.*;
import com.example.petel.entity.PropertyEntity;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.model.ReturnCodeAndDescEnum;
import com.example.petel.repository.PropertyRepository;
import com.example.petel.service.HOTEL002Svc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HOTEL002SvcImpl implements HOTEL002Svc {

    /**
     * PropertyRepository
     */
    private final PropertyRepository propertyRepository;

    /**
     * 單筆旅館查詢
     *
     * @param hotel002Tranrq Req<HOTEL002Tranrq> （propertyId)
     * @return Res<HOTEL002Tranrs>
     * @throws DataNotFoundException
     */
    @Override
    public Res<HOTEL002Tranrs<HOTEL002TranrsHotel>> details(Req<HOTEL002Tranrq> hotel002Tranrq) throws DataNotFoundException {
        log.info("-------- [HOTEL-002] 單筆旅館查詢 ---------");
        Long propertyId = hotel002Tranrq.getTranrq().getId();
        log.info("[HOTEL-002] 查詢 propertyId = {}", propertyId);

        Optional<PropertyEntity> properties = propertyRepository.findById(propertyId);
        if (properties.isEmpty()) {
            log.warn("[HOTEL-002] 依據 propertyId 查無資料");
            throw new DataNotFoundException("查無旅館資料");
        }

        PropertyEntity propertyEntity = properties.get(); // 取出實際物件
        List<HOTEL002TranrsHotel> hotelList = new ArrayList<>();

        HOTEL002TranrsHotel hotel = new HOTEL002TranrsHotel();
        hotel.setName(propertyEntity.getName());
        hotel.setTel(propertyEntity.getTel());
        hotel.setPostalCode(propertyEntity.getPostalCode());
        hotel.setAddress(propertyEntity.getAddress());
        hotelList.add(hotel);
        log.info("[HOTEL-002] 查詢成功，propertyId={}, hotelList={}", propertyId, hotelList);

        HOTEL002Tranrs<HOTEL002TranrsHotel> hotel002Tranrs = new HOTEL002Tranrs<>();
        hotel002Tranrs.setDetail(hotelList);

        return new Res<>(
                new ResMwHeader(ReturnCodeAndDescEnum.SUCCESS),
                hotel002Tranrs
        );
    }
}
