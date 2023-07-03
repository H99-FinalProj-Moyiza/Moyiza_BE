package com.example.moyiza_be.common.handler;

import com.example.moyiza_be.common.utils.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Message> illegalArgumentExceptionHandler(IllegalArgumentException e){
        log.error(e.getMessage());
//        log.error(String.valueOf(e.getCause()));
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Message> nullPointExceptionHandler(NullPointerException e){
        log.error(e.getMessage());
//        log.error(String.valueOf(e.getCause()));
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Message> invalidParameterExceptionHandler(InvalidParameterException e){
        log.error(e.getMessage());
//        log.error(String.valueOf(e.getCause()));
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Message> runtimeExceptionHandler(RuntimeException e){
        log.error(e.getMessage());
//        e.printStackTrace();
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
