package com.czxy.bos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(Exception.class)
    public ResponseEntity<String> defaultErrorHandler(Exception e){
       e.printStackTrace();
       if (e instanceof BosException){
       return  new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
       }
       return  new ResponseEntity<String>("服务器异常，请稍后重试",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
