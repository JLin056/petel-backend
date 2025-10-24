package com.example.petel.service;

import com.example.petel.dto.AUTH005Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.UpdateFailException;

import java.io.IOException;

public interface AUTH005Svc {

    Res<Object> resetPassword(Req<AUTH005Tranrq> req) throws UpdateFailException, IOException;
}
