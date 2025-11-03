package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;

import java.io.IOException;

public interface USER007Svc {

    Res<USER007Tranrs> getOrderDetail(Req<USER007Tranrq> req)
            throws IOException, DataNotFoundException;
}
