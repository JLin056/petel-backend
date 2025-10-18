package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.CHAT001Tranrq;
import com.example.petel.dto.CHAT001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.CHAT001Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class ChatController extends BaseController {

    private final CHAT001Svc chat001Svc;

    @PostMapping("/create")
    public Res<CHAT001Tranrs> createOrGetByOrder(
            @AuthenticationPrincipal AccountPrincipal auth,
            @Valid @RequestBody Req<CHAT001Tranrq> req,
            Errors errors)
            throws InvalidInputException, DataNotFoundException, InsertFailException {
        handleValidForDto(errors);
        return chat001Svc.GetOrCreateByOrder(req, auth);
    }
}
