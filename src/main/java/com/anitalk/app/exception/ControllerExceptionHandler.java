package com.anitalk.app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    private void printExceptionLog(Exception e){
        e.printStackTrace();
        log.error("Exception : {} , message : {}", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseStatusException notFoundExceptionHandler(Exception e){
        printExceptionLog(e);
        return new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseStatusException otherExceptionHandler(Exception e){
        printExceptionLog(e);
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
