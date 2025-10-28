package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.dto.*;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.CHAT001Svc;
import com.example.petel.service.CHAT002Svc;
import com.example.petel.service.CHAT003Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import java.io.IOException;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class ChatController extends BaseController {

    /** CHAT001Svc */
    private final CHAT001Svc chat001Svc;
    /** CHAT002Svc */
    private final CHAT002Svc chat002Svc;
    /** CHAT003Svc */
    private final CHAT003Svc chat003Svc;

    /**
     * 建立聊天室
     * 
     * @param auth
     * @param req
     * @param errors
     * @return
     * @throws InvalidInputException
     * @throws DataNotFoundException
     * @throws InsertFailException
     */
    @PostMapping("/create")
    public Res<CHAT001Tranrs> createOrGetByOrder(
            @AuthenticationPrincipal AccountPrincipal auth,
            @Valid @RequestBody Req<CHAT001Tranrq> req,
            Errors errors)
            throws InvalidInputException, DataNotFoundException, InsertFailException {
        handleValidForDto(errors);
        return chat001Svc.GetOrCreateByOrder(req, auth);
    }

    /**
     * 取得聊天室列表
     * 
     * @param auth
     * @param req
     * @param errors
     * @return
     * @throws IOException
     * @throws InvalidInputException
     */
    @PostMapping("/threads")
    public Res<CHAT002Tranrs> getThreads(
            @AuthenticationPrincipal AccountPrincipal auth,
            @Valid @RequestBody Req<CHAT002Tranrq> req,
            Errors errors)
            throws IOException, InvalidInputException {
        handleValidForDto(errors);
        return chat002Svc.getThreads(auth, req);
    }

    /**
     * 取得單間聊天室訊息內容
     * 
     * @param auth
     * @param req
     * @param errors
     * @return
     * @throws InvalidInputException
     * @throws DataNotFoundException
     * @throws IOException
     */
    @PostMapping("/threads/get")
    public Res<CHAT003Tranrs> getRoom(
            @AuthenticationPrincipal AccountPrincipal auth,
            @RequestBody Req<CHAT003Tranrq> req,
            Errors errors)
            throws InvalidInputException, DataNotFoundException, IOException {
        handleValidForDto(errors);
        return chat003Svc.getRoom(auth, req);
    }
}
