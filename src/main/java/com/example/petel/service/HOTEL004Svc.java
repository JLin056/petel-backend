package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import jakarta.validation.Valid;

import java.io.IOException;

public interface HOTEL004Svc {
    Res<HOTEL004Tranrs<HOTEL004TranrsFacility>> facilities(@Valid Req<HOTEL004Tranrq> hotel004Tranrq) throws DataNotFoundException, IOException;
}
