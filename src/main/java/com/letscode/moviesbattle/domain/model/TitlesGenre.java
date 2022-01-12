package com.letscode.moviesbattle.domain.model;

import com.letscode.moviesbattle.domain.model.type.Genres;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TitlesGenre {
    public Genres genre;
    public List<String> titles;

    public String getRandom() {
        return this.titles.get((int) (Math.random() * this.titles.stream().count()));
    }
}
