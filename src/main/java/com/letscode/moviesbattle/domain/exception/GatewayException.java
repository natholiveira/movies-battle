package com.letscode.moviesbattle.domain.exception;

import org.springframework.http.HttpStatus;

public class GatewayException extends ApiException {

    public GatewayException(String message) {
        super(message);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    ApiError apiError() {
        return ApiError.FAILED_SAVED_IN_CACHE;
    }

    @Override
    String userResponseMessage() {
        return getMessage();
    }
}