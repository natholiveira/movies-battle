package com.letscode.moviesbattle.domain.repository;

import com.letscode.moviesbattle.domain.model.Quizz;
import com.letscode.moviesbattle.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizzRepository extends JpaRepository<Quizz, String> {
    Integer countByUser(User user);
    Boolean existsByUserAndFinished(User user, Boolean finished);
    Optional<Quizz> findByUser_usernameAndFinished(String username, Boolean finished);
}
