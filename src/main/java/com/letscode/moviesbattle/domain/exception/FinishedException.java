package com.letscode.moviesbattle.domain.exception;

import org.springframework.http.HttpStatus;

public class FinishedException extends ApiException {

    public FinishedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_ACCEPTABLE;
    }

    @Override
    ApiError apiError() {
        return ApiError.QUIZZ_ROUND_NOT_FINISHED;
    }

    @Override
    String userResponseMessage() {
        return getMessage();
    }
}