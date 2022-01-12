package com.letscode.moviesbattle.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letscode.moviesbattle.domain.repository.QuizzRepository;
import com.letscode.moviesbattle.web.request.LoginRequest;
import com.letscode.moviesbattle.web.request.UserRequest;
import com.letscode.moviesbattle.web.response.QuizzResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuizzControllerTest {

    @Autowired
    QuizzRepository quizzRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String username  = "teste1";
    private static final String password  = "teste1";
    private static final String urlStart  = "/api/quizz/start";
    private static final String urlFinished  = "/api/quizz/finished";

    @Test
    public void startQuizz() throws JsonProcessingException {
        var userRequest = new UserRequest(username, "teste", password);
        restTemplate.postForEntity("/user", userRequest, String.class);

        var loginRequest = new LoginRequest(username, password);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", loginRequest, String.class);

        var token = loginResponse.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+token);

        var entity = new HttpEntity<>(headers);

        ResponseEntity<String> quizzResponseEntity = restTemplate.exchange(urlStart, HttpMethod.GET, entity, String.class);
        var quizzResponse = new ObjectMapper().readValue(quizzResponseEntity.getBody(), QuizzResponse.class);

        assertEquals(quizzResponseEntity.getStatusCode(), HttpStatus.OK);
        assertFalse(quizzResponse.getFinished());
        assertEquals(quizzResponse.getCurrentRankingPosition(), 0);
        assertEquals(quizzResponse.getCurrentScore(), 0);
    }

    @Test
    public void finishQuizz() throws JsonProcessingException {
        var userRequest = new UserRequest(username, "teste", password);
        restTemplate.postForEntity("/user", userRequest, String.class);

        var loginRequest = new LoginRequest(username, password);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", loginRequest, String.class);

        var token = loginResponse.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+token);

        var entity = new HttpEntity<>(headers);

        ResponseEntity<String> quizzResponseEntity = restTemplate.exchange(urlFinished, HttpMethod.POST, entity, String.class);
        var quizzResponse = new ObjectMapper().readValue(quizzResponseEntity.getBody(), QuizzResponse.class);

        assertEquals(quizzResponseEntity.getStatusCode(), HttpStatus.OK);
        assertTrue(quizzResponse.getFinished());
    }
}
