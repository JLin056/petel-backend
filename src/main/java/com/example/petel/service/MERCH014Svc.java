package com.example.petel.service;

import com.example.petel.dto.MERCH014Tranrq;
import com.example.petel.dto.MERCH014Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.UpdateFailException;

public interface MERCH014Svc {
    Res<MERCH014Tranrs> updateStatus(Req<MERCH014Tranrq> requestBody)
            throws DataNotFoundException, UpdateFailException;
}
