package com.example.petel.controller.advice;

import com.example.petel.dto.Res;
import com.example.petel.dto.ResMwHeader;
import com.example.petel.exception.*;
import com.example.petel.model.ReturnCodeAndDescEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebExceptionHandler {

    /**
     * 使用者欄位填寫不完整異常處理
     */
    @ResponseBody
    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.OK)
    public Res<Object> handleInvalidInputException(InvalidInputException ex) {
        ResMwHeader resMwHeader = new ResMwHeader();
        String message = ex.getMessage();
        if (message.isBlank()) {
            message = "輸入值不得為空";
        }
        resMwHeader.setReturnCode(ReturnCodeAndDescEnum.ERROR_INPUT.getCode());
        resMwHeader.setReturnDesc(message);
        return new Res<>(resMwHeader, null);
    }

    /**
     * 使用者欄位必填不完整異常處理
     */
    @ResponseBody
    @ExceptionHandler(ErrorInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res<Object> handleErrorInputException(ErrorInputException ex) {
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.ERROR_INPUT), null);
    }

    /**
     * 查無資料異常處理
     */
    @ResponseBody
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public Res<Object> handleDataNotFoundException(DataNotFoundException ex) {
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.DATA_NOT_FOUND), null);
    }

    /**
     * 新增異常
     */
    @ResponseBody
    @ExceptionHandler(InsertFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleInsertFailException(InsertFailException ex) {
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.INSERT_FAIL), null);
    }

    /**
     * 更新異常
     */
    @ResponseBody
    @ExceptionHandler(UpdateFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleUpdateFailException(UpdateFailException ex) {
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.UPDATE_FAIL), null);
    }

    /**
     * 刪除異常
     */
    @ResponseBody
    @ExceptionHandler(DeleteFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleDeleteFailException(DeleteFailException ex) {
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.DELETE_FAIL), null);
    }

    /**
     * 其他系統異常
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleException(Exception ex) {
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.S9999), null);
    }

    /**
     * JWT 異常
     */
    @ResponseBody
    @ExceptionHandler(JwtProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleJwtProcessingException(JwtProcessingException ex) {
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.S9999), null);
    }

    /**
     * 退款異常
     */
    @ResponseBody
    @ExceptionHandler(RefundFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleRefundFailException(RefundFailException ex) {
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.S9999), null);
    }

    /**
     * 付款方式異常
     */
    @ResponseBody
    @ExceptionHandler(InvalidPaymentMethodException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleInvalidPaymentMethodException(InvalidPaymentMethodException ex) {
        return new Res<>(new ResMwHeader(ReturnCodeAndDescEnum.S9999), null);
    }
}


