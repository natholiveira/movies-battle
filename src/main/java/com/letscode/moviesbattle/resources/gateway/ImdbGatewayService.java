package com.letscode.moviesbattle.resources.gateway;

import com.letscode.moviesbattle.domain.model.type.Genres;
import com.letscode.moviesbattle.web.response.RoundResponse;
import com.letscode.moviesbattle.web.response.TitlesGenreResponse;

import java.io.IOException;

public interface ImdbGatewayService {
    TitlesGenreResponse getTitlesByGenre(Genres genre) throws IOException, InterruptedException;
    RoundResponse getDetailsOfTitles(String title01, String title02) throws IOException, InterruptedException;
}
