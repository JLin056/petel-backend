package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;

import java.io.IOException;

public interface HOTEL003Svc {
    Res<HOTEL003Tranrs<HOTEL003TranrsRoom>> rooms(Req<HOTEL003Tranrq> hotel003Tranrq) throws DataNotFoundException, IOException;
}
