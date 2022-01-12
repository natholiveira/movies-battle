package com.letscode.moviesbattle.web.controller;

import com.letscode.moviesbattle.domain.exception.FinishedException;
import com.letscode.moviesbattle.domain.exception.NotFinishedException;
import com.letscode.moviesbattle.domain.exception.NotFoundException;
import com.letscode.moviesbattle.web.response.QuizzResponse;
import com.letscode.moviesbattle.domain.service.QuizzService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
public class QuizzController {

    @Autowired
    QuizzService quizzService;

    private static final String USERNAME_ATTRIBUTE = "USERNAME";

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Start quizz game.", authorizations = { @Authorization(value = "Bearer {token}")} )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Start quizz"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 406, message = "There are unfinished rounds and/or quizzes"),
            @ApiResponse(code = 403, message = "User not authenticated")
    })
    @GetMapping("/api/quizz/start")
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public QuizzResponse start() throws NotFoundException, NotFinishedException {
        var username = RequestContextHolder.getRequestAttributes().getAttribute(
                USERNAME_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST
        ).toString();
        return quizzService.start(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Finish quizz game.", authorizations = { @Authorization(value = "Bearer {token}")} )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Finish quizz"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 406, message = "No open quizz found for the user logged."),
            @ApiResponse(code = 403, message = "User not authenticated")
    })
    @PostMapping("/api/quizz/finished")
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public QuizzResponse answer() throws NotFoundException, FinishedException {
        var username = RequestContextHolder.getRequestAttributes().getAttribute(
                USERNAME_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST
        ).toString();
        return quizzService.finish(username);
    }
}
