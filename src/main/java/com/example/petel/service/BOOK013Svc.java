package com.example.petel.service;

import com.example.petel.dto.BOOK013Tranrq;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidPaymentMethodException;
import com.example.petel.exception.UpdateFailException;

public interface BOOK013Svc {
    Res<Object> book013(Req<BOOK013Tranrq> requestBody) throws DataNotFoundException, UpdateFailException, InvalidPaymentMethodException, InsertFailException;
}