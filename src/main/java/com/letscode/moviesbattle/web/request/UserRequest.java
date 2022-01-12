package com.letscode.moviesbattle.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserRequest {

    @NotNull
    @NotBlank
    String username;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    @NotBlank
    String password;
}
