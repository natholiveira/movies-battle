package com.letscode.moviesbattle.domain.model;

import com.letscode.moviesbattle.web.response.TitlesResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "title")
public class Title {
    @Id
    String id;

    @Column(name = "title")
    String title;

    @Column(name = "rating")
    Double rating;

    @Column(name = "year")
    Integer year;

    public Title(TitlesResponse titlesResponse) {
        this.id = titlesResponse.getId();
        this.title = titlesResponse.getTitle();
        this.rating = titlesResponse.getRating();
        this.year = titlesResponse.getYear();
    }
}
