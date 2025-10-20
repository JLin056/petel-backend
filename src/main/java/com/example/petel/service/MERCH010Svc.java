package com.example.petel.service;

import com.example.petel.dto.MERCH010Tranrq;
import com.example.petel.dto.MERCH010Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;
import jakarta.validation.Valid;

public interface MERCH010Svc {
    Res<MERCH010Tranrs> editSeller(@Valid Req<MERCH010Tranrq> merch010Tranrq) throws DataNotFoundException, UpdateFailException;
}
