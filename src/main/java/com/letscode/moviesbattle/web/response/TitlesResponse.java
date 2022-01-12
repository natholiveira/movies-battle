package com.letscode.moviesbattle.web.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.letscode.moviesbattle.domain.model.Title;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TitlesResponse {
    String id;
    String title;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Double rating;
    Integer year;

    public TitlesResponse(Title title) {
        this.id = title.getId();
        this.title = title.getTitle();
        this.year = title.getYear();
    }
}