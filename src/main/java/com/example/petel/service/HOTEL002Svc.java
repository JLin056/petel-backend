package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;

public interface HOTEL002Svc {
    Res<HOTEL002Tranrs<HOTEL002TranrsHotel>> details(Req<HOTEL002Tranrq> hotel002Tranrq) throws DataNotFoundException, UpdateFailException;
}

