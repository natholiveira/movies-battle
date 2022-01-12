package com.letscode.moviesbattle.domain.service;

import com.letscode.moviesbattle.domain.exception.FinishedException;
import com.letscode.moviesbattle.domain.exception.NotFinishedException;
import com.letscode.moviesbattle.domain.exception.NotFoundException;
import com.letscode.moviesbattle.web.response.QuizzResponse;

public interface QuizzService {
    QuizzResponse start(String username) throws NotFoundException, NotFinishedException;
    QuizzResponse finish(String username) throws NotFoundException, FinishedException;
}
