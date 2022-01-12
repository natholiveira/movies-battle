package com.letscode.moviesbattle.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "game_match")
public class Quizz {
    @Id
    String id;

    @Column(name = "score")
    Long points = 0L;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false, referencedColumnName = "id")
    User user;

    @Column(name = "erros")
    Long errors = 0L;

    @Column(name = "finished")
    Boolean finished = false;

    public Quizz(User user) {
        this.id= UUID.randomUUID().toString();
        this.user=user;
    }
}
