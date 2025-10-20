package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InvalidInputException;

public interface HOTEL001Svc {
    Res<HOTEL001Tranrs<HOTEL001TranrsHotel>> queryHotels(Req<HOTEL001Tranrq> hotel001Tranrq) throws DataNotFoundException, InvalidInputException;
}
