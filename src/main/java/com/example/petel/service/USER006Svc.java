package com.example.petel.service;

import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.USER006Tranrq;
import com.example.petel.dto.USER006Tranrs;
import com.example.petel.exception.DataNotFoundException;

import java.io.IOException;

public interface USER006Svc {
    Res<USER006Tranrs> getBookingList(String accountId, Req<USER006Tranrq> req)
            throws IOException, DataNotFoundException;
}
