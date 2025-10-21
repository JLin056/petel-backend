package com.example.petel.service;

import com.example.petel.dto.Res;
import com.example.petel.dto.USER004Tranrs;
import com.example.petel.exception.DataNotFoundException;

public interface USER004Svc {

    Res<USER004Tranrs> getUserInfo(String accountId) throws DataNotFoundException;
}
