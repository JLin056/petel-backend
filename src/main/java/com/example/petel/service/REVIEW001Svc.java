package com.example.petel.service;

import com.example.petel.dto.REVIEW001Tranrq;
import com.example.petel.dto.REVIEW001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;

public interface REVIEW001Svc {

    Res<REVIEW001Tranrs> createReview(String accountId, Req<REVIEW001Tranrq> req)
            throws DataNotFoundException, InvalidInputException, InsertFailException;
}
