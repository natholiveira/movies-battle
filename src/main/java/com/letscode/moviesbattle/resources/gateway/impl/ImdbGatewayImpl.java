package com.letscode.moviesbattle.resources.gateway.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letscode.moviesbattle.resources.gateway.ImdbGatewayService;
import com.letscode.moviesbattle.domain.model.type.Genres;
import com.letscode.moviesbattle.resources.properties.ImdbProperties;
import com.letscode.moviesbattle.web.response.RoundResponse;
import com.letscode.moviesbattle.web.response.TitlesGenreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Service
public class ImdbGatewayImpl implements ImdbGatewayService {

    @Autowired
    private ImdbProperties imdbProperties;

    public static final String HOST_HEADER = "x-rapidapi-host";
    public static final String API_KEY_HEADER = "x-rapidapi-key";

    @Override
    public TitlesGenreResponse getTitlesByGenre(Genres genre) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imdbProperties.url+"/title/get-popular-movies-by-genre?genre"+genre))
                .header(HOST_HEADER, imdbProperties.host)
                .header(API_KEY_HEADER, imdbProperties.apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();


        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        var titles = new ObjectMapper().readValue(response.body(), ArrayList.class);

        return new TitlesGenreResponse(titles);
    }

    @Override
    public RoundResponse getDetailsOfTitles(String firstTitle, String secondTitle) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(imdbProperties.url+"/title/get-meta-data?ids="+firstTitle+"&ids="+secondTitle)))
                .header(HOST_HEADER, imdbProperties.host)
                .header(API_KEY_HEADER, imdbProperties.apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return new ObjectMapper().readValue(response.body(), RoundResponse.class);
    }
}
