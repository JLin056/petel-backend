package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.REVIEW001Tranrq;
import com.example.petel.dto.REVIEW001Tranrs;
import com.example.petel.dto.Req;
import com.example.petel.dto.Res;
import com.example.petel.exception.DataNotFoundException;
import com.example.petel.exception.InsertFailException;
import com.example.petel.exception.InvalidInputException;
import com.example.petel.model.jwt.AccountPrincipal;
import com.example.petel.service.REVIEW001Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class ReviewController extends BaseController {

    private final REVIEW001Svc review001Svc;

    /**
     * 新增評論
     * @param auth
     * @param req
     * @param errors
     * @return
     * @throws InvalidInputException
     */
    @PostMapping("/create")
    public Res<REVIEW001Tranrs> createReview(@AuthenticationPrincipal AccountPrincipal auth,
                                             @Valid @RequestBody Req<REVIEW001Tranrq> req,
                                             Errors errors)
            throws InvalidInputException, DataNotFoundException, InsertFailException {
        handleValidForDto(errors);
        return review001Svc.createReview(auth.getAccountId(), req);
    }
}
