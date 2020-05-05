package com.mock.core.exception;

import com.mock.common.exception.ExceptionPlus;
import com.mock.common.pojo.JsonPublic;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public JsonPublic nullPointerExceptionHandler(Exception e){
        if (e instanceof ExceptionPlus){
            ExceptionPlus ep = (ExceptionPlus) e;
            return new JsonPublic(ep.getCode(), ep.getMessage());
        }else {
            return new JsonPublic(e);
        }
    }

}