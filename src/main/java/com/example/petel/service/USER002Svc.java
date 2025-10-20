package com.example.petel.service;

import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.dto.USER002Tranrq;
import com.example.petel.dto.USER002Tranrs;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface USER002Svc {

    Res<USER002Tranrs> updateUser(String accountId, Req<USER002Tranrq> req)
            throws DataNotFoundException, JsonMappingException, InsertFailException;
}
