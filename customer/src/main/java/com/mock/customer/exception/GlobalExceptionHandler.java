package com.mock.customer.exception;

import com.mock.common.pojo.JsonPublic;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public JsonPublic nullPointerExceptionHandler(Exception e){
        return new JsonPublic(e);
    }

}
