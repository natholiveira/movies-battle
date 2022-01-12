package com.letscode.moviesbattle.domain.service;

import com.letscode.moviesbattle.domain.exception.FinishedException;
import com.letscode.moviesbattle.domain.exception.GatewayException;
import com.letscode.moviesbattle.domain.exception.NotFinishedException;
import com.letscode.moviesbattle.domain.exception.NotFoundException;
import com.letscode.moviesbattle.web.request.RoundAnswerRequest;
import com.letscode.moviesbattle.web.response.RoundResponse;
import com.letscode.moviesbattle.web.response.RoundResultResponse;

import java.io.IOException;

public interface RoundService {
    RoundResponse getPair(String quizzId) throws IOException, GatewayException, InterruptedException, NotFoundException, NotFinishedException, FinishedException;
    RoundResultResponse saveAnswer(RoundAnswerRequest roundAnswerRequest, String username) throws NotFoundException, FinishedException;
}
