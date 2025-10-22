package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import jakarta.validation.Valid;

public interface MERCH002Svc {
    Res<MERCH002Tranrs<MERCH002TranrsRoom>> roomList(@Valid Req<MERCH002Tranrq> merch002Tranrq) throws DataNotFoundException;
}
