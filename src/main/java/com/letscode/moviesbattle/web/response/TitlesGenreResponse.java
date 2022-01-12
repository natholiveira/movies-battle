package com.letscode.moviesbattle.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TitlesGenreResponse {
    List<String> titles;
}