package com.example.petel.service;

import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import jakarta.validation.Valid;

public interface MERCH003Svc {
    Res<MERCH003Tranrs<MERCH003TranrsReview>> reviews(@Valid Req<MERCH003Tranrq> merch003Tranrq) throws DataNotFoundException;
}
