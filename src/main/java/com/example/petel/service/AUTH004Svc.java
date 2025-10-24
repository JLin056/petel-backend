package com.example.petel.service;

import com.example.petel.dto.AUTH004Tranrq;
import com.example.petel.dto.AUTH004Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;

public interface AUTH004Svc {
    Res<AUTH004Tranrs> forgotPassword(Req<AUTH004Tranrq> req);
}
