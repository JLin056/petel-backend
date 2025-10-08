package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.AUTH001Tranrq;
import com.example.petel.dto.AUTH001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.service.AUTH001Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
public class AuthController extends BaseController{
    /** AUTH001 Service */
    private final AUTH001Svc auth001Svc;

    @PostMapping("/register")
    public Res<AUTH001Tranrs> register(@Valid @RequestBody Req<AUTH001Tranrq> req, Errors errors)
            throws InsertFailException, InvalidInputException {
        handleValidForDto(errors);
        return auth001Svc.register(req);
    }
}
