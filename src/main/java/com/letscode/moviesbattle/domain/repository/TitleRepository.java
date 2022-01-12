package com.letscode.moviesbattle.domain.repository;

import com.letscode.moviesbattle.domain.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitleRepository extends JpaRepository<Title, String> {
}
