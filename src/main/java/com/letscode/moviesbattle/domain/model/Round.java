package com.letscode.moviesbattle.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "round")
public class Round {
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String id;

    @ManyToOne
    @JoinColumn(name="first_title_id", nullable=false, referencedColumnName = "id")
    Title firstTitle;

    @ManyToOne
    @JoinColumn(name="second_title_id", nullable=false, referencedColumnName = "id")
    Title secondTitle;

    @Column(name = "answered_correctly")
    Boolean answeredCorrectly = false;

    @Column(name = "finished")
    Boolean finished = false;

    @ManyToOne
    @JoinColumn(name="quizz_id", nullable=false, referencedColumnName = "id")
    Quizz quizz;
    
    public Round(Quizz quizz, Title firstTitle, Title secondTitle) {
        this.id = UUID.randomUUID().toString();
        this.firstTitle = firstTitle;
        this.secondTitle = secondTitle;
        this.quizz = quizz;
    }
}
