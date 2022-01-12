package com.letscode.moviesbattle.domain.service.impl;

import com.letscode.moviesbattle.domain.exception.DuplicatedUserException;
import com.letscode.moviesbattle.domain.model.User;
import com.letscode.moviesbattle.domain.repository.UserRepository;
import com.letscode.moviesbattle.web.request.UserRequest;
import com.letscode.moviesbattle.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User create(UserRequest userRequest) throws DuplicatedUserException {

        if (userRepository.findByUsername(userRequest.getUsername()).isPresent())
            throw new DuplicatedUserException(userRequest.getUsername());

        var password = passwordEncoder.encode(userRequest.getPassword());

        return userRepository.save(new User(userRequest, password));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getRanking() {
        return userRepository.findAllOderByRankingPositionDesc();
    }
}
