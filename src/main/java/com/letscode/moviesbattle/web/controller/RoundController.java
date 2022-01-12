package com.letscode.moviesbattle.web.controller;

import com.letscode.moviesbattle.domain.exception.FinishedException;
import com.letscode.moviesbattle.domain.exception.GatewayException;
import com.letscode.moviesbattle.domain.exception.NotFinishedException;
import com.letscode.moviesbattle.domain.exception.NotFoundException;
import com.letscode.moviesbattle.web.request.RoundAnswerRequest;
import com.letscode.moviesbattle.web.response.RoundResponse;
import com.letscode.moviesbattle.web.response.RoundResultResponse;
import com.letscode.moviesbattle.domain.service.RoundService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class RoundController {

    @Autowired
    RoundService roundService;

    private static final String USERNAME_ATTRIBUTE = "USERNAME";

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get new round", authorizations = { @Authorization(value = "Bearer {token}")} )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return new round"),
            @ApiResponse(code = 404, message = "Not found opened quizz for User"),
            @ApiResponse(code = 406, message = "This quizz is finished"),
            @ApiResponse(code = 403, message = "User not authenticated")
    })
    @GetMapping("/api/round")
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public RoundResponse getTitlePair() throws InterruptedException, FinishedException, IOException, NotFinishedException, NotFoundException, GatewayException {
        var username = RequestContextHolder.getRequestAttributes().getAttribute(
                USERNAME_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST
        ).toString();
        return roundService.getPair(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Answer round", authorizations = { @Authorization(value = "Bearer {token}")} )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Round answered"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 406, message = "No open round found for the logged in user"),
            @ApiResponse(code = 403, message = "User not authenticated")
    })
    @PostMapping("/api/round/answer")
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public RoundResultResponse answer(
            @RequestBody @Valid RoundAnswerRequest roundAnswerRequest
    ) throws NotFoundException, FinishedException {
        var username = RequestContextHolder.getRequestAttributes().getAttribute(
                USERNAME_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST
        ).toString();
        return roundService.saveAnswer(roundAnswerRequest, username);
    }
}
