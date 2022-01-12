package com.letscode.moviesbattle.domain.repository;

import com.letscode.moviesbattle.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM user WHERE score < ?1 order by ranking_position DESC", nativeQuery = true)
    List<User> findAllByScoreLessThanOderByDesc(Long score);

    @Query(value = "SELECT * FROM user order by ranking_position", nativeQuery = true)
    List<User> findAllOderByRankingPositionDesc();
}
