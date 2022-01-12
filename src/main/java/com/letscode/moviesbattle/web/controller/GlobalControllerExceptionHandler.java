package com.letscode.moviesbattle.web.controller;

import com.letscode.moviesbattle.domain.exception.ApiException;
import com.letscode.moviesbattle.domain.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(ApiException ex) {
        return new ResponseEntity<>(ex.createErrorResponse(), ex.httpStatus());
    }
}
