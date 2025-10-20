package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import jakarta.validation.Valid;

import java.io.IOException;

public interface MERCH001Svc {
    Res<MERCH001Tranrs<MERCH001TranrsBooking>> list(@Valid Req<MERCH001Tranrq> merch001Tranrq) throws DataNotFoundException, IOException;
}
