package com.letscode.moviesbattle.web.controller;

import com.letscode.moviesbattle.domain.exception.DuplicatedUserException;
import com.letscode.moviesbattle.domain.model.User;
import com.letscode.moviesbattle.web.request.UserRequest;
import com.letscode.moviesbattle.domain.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created user"),
            @ApiResponse(code = 409, message = "Duplicated user"),
            @ApiResponse(code = 403, message = "User not authenticated")
    })
    @PostMapping
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public User create(@RequestBody @Valid UserRequest userRequest) throws DuplicatedUserException {
        return userService.create(userRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get all users")
    })
    @GetMapping
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public List<User> getAll() {
        return userService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get ranking", authorizations = { @Authorization(value = "Bearer {token}")})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get ranking")
    })
    @GetMapping("/ranking")
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public List<User> getRanking() {
        return userService.getRanking();
    }
}
