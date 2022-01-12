package com.letscode.moviesbattle.domain.service;

import com.letscode.moviesbattle.domain.exception.DuplicatedUserException;
import com.letscode.moviesbattle.domain.model.User;
import com.letscode.moviesbattle.web.request.UserRequest;

import java.util.List;

public interface UserService {
    User create(UserRequest userRequest) throws DuplicatedUserException;
    List<User> getAll();
    List<User> getRanking();
}
