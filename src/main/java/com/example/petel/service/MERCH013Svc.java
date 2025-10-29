package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import jakarta.validation.Valid;

public interface MERCH013Svc {
    Res<MERCH013Tranrs<MERCH013TranrsProperty>> getProperties(@Valid Req<MERCH013Tranrq> merch013Tranrq) throws DataNotFoundException;
}
