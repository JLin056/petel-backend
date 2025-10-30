package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;

public interface HOTEL006Svc {
    Res<HOTEL006Tranrs> singleHotelDetail(Req<HOTEL006Tranrq> hotel006Tranrq)
        throws DataNotFoundException, InvalidInputException;
}
