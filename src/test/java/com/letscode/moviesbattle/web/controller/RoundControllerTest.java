package com.letscode.moviesbattle.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letscode.moviesbattle.web.request.LoginRequest;
import com.letscode.moviesbattle.web.request.UserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RoundControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String username  = "teste";
    private static final String password  = "teste";
    private static final String url  = "/api/round";
    private static final String quizzUrl  = "/api/quizz/start";

    @Test
    public void getRound() throws JsonProcessingException {
        var userRequest = new UserRequest(username, "teste", password);
        restTemplate.postForEntity("/user", userRequest, String.class);

        var loginRequest = new LoginRequest(username, password);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/login", loginRequest, String.class);

        var token = loginResponse.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+token);

        var entity = new HttpEntity<>(headers);

        restTemplate.exchange(quizzUrl, HttpMethod.GET, entity, String.class);

        ResponseEntity<String> roundResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        var firstTitle = new ObjectMapper().readTree(roundResponseEntity.getBody()).get("title01");
        var secondTitle = new ObjectMapper().readTree(roundResponseEntity.getBody()).get("title02");

        assertEquals(roundResponseEntity.getStatusCode(), HttpStatus.OK);
        assertNotEquals(firstTitle, secondTitle);
    }
}
