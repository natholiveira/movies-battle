package com.letscode.moviesbattle.domain.service.impl;

import com.letscode.moviesbattle.domain.exception.FinishedException;
import com.letscode.moviesbattle.domain.exception.NotFinishedException;
import com.letscode.moviesbattle.domain.exception.NotFoundException;
import com.letscode.moviesbattle.domain.model.Quizz;
import com.letscode.moviesbattle.domain.model.User;
import com.letscode.moviesbattle.resources.properties.ScoreProperties;
import com.letscode.moviesbattle.domain.repository.QuizzRepository;
import com.letscode.moviesbattle.domain.repository.RoundRepository;
import com.letscode.moviesbattle.domain.repository.UserRepository;
import com.letscode.moviesbattle.web.response.QuizzResponse;
import com.letscode.moviesbattle.domain.service.QuizzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizzServiceImpl implements QuizzService {

    @Autowired
    QuizzRepository quizzRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoundRepository roundRepository;

    @Autowired
    ScoreProperties scoreProperties;

    @Override
    public QuizzResponse start(String username) throws NotFoundException, NotFinishedException {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User with username "+username+" not found")
        );

        if (quizzRepository.existsByUserAndFinished(user, false) ||
                roundRepository.existsByQuizz_UserAndFinished(user, false)) {
            throw new NotFinishedException("There are unfinished rounds and/or quizzes");
        }

        var quizz = quizzRepository.save(new Quizz(user));

        return new QuizzResponse(
                quizz.getId(),
                user.getScore(),
                scoreProperties.maximunAttempts,
                0L,
                false,
                user.getRankingPosition()
        );
    }

    @Override
    public QuizzResponse finish(String username) throws NotFoundException, FinishedException {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User with username "+username+" not found.")
        );

        var quizz = quizzRepository.findByUser_usernameAndFinished(username, false).orElseThrow(
                () -> new FinishedException("No open quizz found for the logged in user.")
        );

        updateRanking(user);

        quizz.setFinished(true);

        quizzRepository.save(quizz);

        return new QuizzResponse(
                quizz.getId(),
                user.getScore(),
                scoreProperties.maximunAttempts,
                quizz.getErrors(),
                true,
                user.getRankingPosition()
        );
    }

    private void updateRanking(User user) {
        var usersCount = userRepository.count();
        var usersScoreLassThan = userRepository.findAllByScoreLessThanOderByDesc(user.getScore());

        if (usersCount == 1) {
            user.setRankingPosition(1L);
        } else if (usersCount > 1 && usersScoreLassThan.isEmpty()) {
            user.setRankingPosition(usersCount+1);
        } else if (!usersScoreLassThan.isEmpty()) {
            user.setRankingPosition(usersScoreLassThan.stream().findFirst().get().getRankingPosition());

            usersScoreLassThan.stream().forEach(userUpdate -> {
                userUpdate.setRankingPosition(userUpdate.getRankingPosition() + 1);
                userRepository.save(userUpdate);
            });

            userRepository.save(user);
        }
    }
}
