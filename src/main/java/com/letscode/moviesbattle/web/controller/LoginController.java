package com.letscode.moviesbattle.web.controller;

import com.letscode.moviesbattle.web.request.LoginRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @ApiOperation("Login Example")
    @PostMapping("/login")
    void fakeLogin(@RequestBody LoginRequest loginRequest) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }
}
