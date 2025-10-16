package com.example.petel.controller;

import com.example.petel.controller.advice.BaseController;
import com.example.petel.dto.*;
import com.example.petel.exception.*;
import com.example.petel.service.MERCH003Svc;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchants")
@CrossOrigin("http://localhost:4200")
public class MerchController extends BaseController {

    /**
     * MERCH003 Service
     */
    private final MERCH003Svc merch003Svc;

    @PostMapping(value = "/hotels/reviews")
    public Res<MERCH003Tranrs<MERCH003TranrsReview>> reviews(@Valid @RequestBody Req<MERCH003Tranrq> merch003Tranrq, Errors errors)
            throws DataNotFoundException, InvalidInputException, IOException {
        handleValidForDto(errors);
        return merch003Svc.reviews(merch003Tranrq);
    }
}
