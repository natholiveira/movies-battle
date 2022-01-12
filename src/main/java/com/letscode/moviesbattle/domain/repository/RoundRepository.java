package com.letscode.moviesbattle.domain.repository;

import com.letscode.moviesbattle.domain.model.Quizz;
import com.letscode.moviesbattle.domain.model.Round;
import com.letscode.moviesbattle.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round, String> {
    List<Round> findAllByQuizz(Quizz quizz);
    Boolean existsByQuizz_UserAndFinished(User user, Boolean finished);
    Boolean existsByFirstTitle_idAndSecondTitle_id(String firstTitleId, String secondTitleId);
    Round findByQuizzAndFinished(Quizz quizz, Boolean finished);
    Optional<Round> findByQuizz_UserAndFinished(User user, Boolean finished);
}
