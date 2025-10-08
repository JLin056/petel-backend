package com.example.petel.controller.advice;

import com.example.petel.exception.InvalidInputException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;

import java.util.stream.Collectors;

public class BaseController {

    protected static void handleValidForDto(Errors errors) throws InvalidInputException {
        if (errors.hasErrors()) {
            throw new InvalidInputException(errors.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(" & ")));
        }
    }

}