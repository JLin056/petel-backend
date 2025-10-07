package com.example.petel.controller.advice;

import com.example.petel.dto.MwHeader;
import com.example.petel.dto.Res;
import com.example.petel.model.ReturnCodeAndDescEnum;
import org.springframework.http.HttpStatus;
import com.example.petel.exception.*;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res<Object> handleInvalidInputException(InvalidInputException ex) {
        return new Res<Object>(new MwHeader(ReturnCodeAndDescEnum.ERROR_INPUT), new Object());
    }

    /**
     * 使用者欄位必填不完整異常處理
     */
    @ResponseBody
    @ExceptionHandler(ErrorInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Res<Object> handleErrorInputException(ErrorInputException ex) {
        return new Res<Object>(new MwHeader(ReturnCodeAndDescEnum.ERROR_INPUT), new Object());
    }

    /**
     * 查無資料異常處理
     */
    @ResponseBody
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public Res<Object> handleDataNotFoundException(DataNotFoundException ex) {
        return new Res<Object>(new MwHeader(ReturnCodeAndDescEnum.DATA_NOT_FOUND), new Object());
    }


    /**
     * 新增異常
     */
    @ResponseBody
    @ExceptionHandler(InsertFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleInsertFailException(InsertFailException ex) {
        return new Res<Object>(new MwHeader(ReturnCodeAndDescEnum.INSERT_FAIL), new Object());
    }


    /**
     * 更新異常
     */
    @ResponseBody
    @ExceptionHandler(UpdateFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleUpdateFailException(UpdateFailException ex) {
        return new Res<Object>(new MwHeader(ReturnCodeAndDescEnum.UPDATE_FAIL), new Object());
    }

    /**
     * 刪除異常
     */
    @ResponseBody
    @ExceptionHandler(DeleteFailException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleDeleteFailException(DeleteFailException ex) {
        return new Res<Object>(new MwHeader(ReturnCodeAndDescEnum.DELETE_FAIL), new Object());
    }

    /**
     * 其他系統異常
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Res<Object> handleException(Exception ex) {
        return new Res<Object>(new MwHeader(ReturnCodeAndDescEnum.S9999), new Object());
    }
}


