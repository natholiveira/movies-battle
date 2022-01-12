package com.letscode.moviesbattle.domain.service;

import com.letscode.moviesbattle.domain.exception.FinishedException;
import com.letscode.moviesbattle.domain.exception.GatewayException;
import com.letscode.moviesbattle.domain.exception.NotFinishedException;
import com.letscode.moviesbattle.domain.exception.NotFoundException;
import com.letscode.moviesbattle.resources.gateway.ImdbGatewayService;
import com.letscode.moviesbattle.domain.model.Quizz;
import com.letscode.moviesbattle.domain.model.Round;
import com.letscode.moviesbattle.domain.model.Title;
import com.letscode.moviesbattle.domain.model.User;
import com.letscode.moviesbattle.resources.properties.ScoreProperties;
import com.letscode.moviesbattle.domain.repository.QuizzRepository;
import com.letscode.moviesbattle.domain.repository.RoundRepository;
import com.letscode.moviesbattle.domain.repository.TitleRepository;
import com.letscode.moviesbattle.domain.repository.UserRepository;
import com.letscode.moviesbattle.web.request.RoundAnswerRequest;
import com.letscode.moviesbattle.web.request.UserRequest;
import com.letscode.moviesbattle.web.response.RoundResponse;
import com.letscode.moviesbattle.web.response.TitlesGenreResponse;
import com.letscode.moviesbattle.web.response.TitlesResponse;
import com.letscode.moviesbattle.domain.service.impl.RoundServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RoundServiceTest {

    @Mock
    ImdbGatewayService imdbGatewayService;

    @Mock
    RoundRepository roundRepository;

    @Mock
    QuizzRepository quizzRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    QuizzService quizzService;

    @Mock
    TitleRepository titleRepository;

    @InjectMocks
    RoundServiceImpl service;

    @Mock
    ScoreProperties scoreProperties;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void returnExceptionWhenNotFoundOpenQuizzToLoggedUser() {
        when(quizzRepository.findByUser_usernameAndFinished(eq("teste"), eq(false))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getPair("teste"));
    }

    @Test
    public void returnExceptionWhenNotFoundOpenRoundToLoggedUser() {
        var user = new User(new UserRequest("teste", "teste", "teste"), "teste");
        var quizz = new Quizz("id", 0L, user, 0L, true);

        when(quizzRepository.findByUser_usernameAndFinished(eq("teste"), eq(false))).thenReturn(Optional.of(quizz));
        when(roundRepository.existsByQuizz_UserAndFinished(eq(user), eq(false))).thenReturn(false);

        assertThrows(FinishedException.class, () -> service.getPair("teste"));
    }

    @Test
    public void returnValidRoundResponse() throws IOException, InterruptedException, NotFinishedException, FinishedException, GatewayException, NotFoundException {
        var validTitlesGenre = Arrays.asList("/title/tt12345/", "/title/tt12345/", "/title/tt12346/");
        var roundResponse = getRoundResponse(8.0, 7.0);

        when(quizzRepository.findByUser_usernameAndFinished(eq("teste"), eq(false))).thenReturn(Optional.of(new Quizz()));
        when(roundRepository.existsByQuizz_UserAndFinished(any(), any())).thenReturn(false);

        when(imdbGatewayService.getTitlesByGenre(any())).thenReturn(new TitlesGenreResponse(validTitlesGenre));
        when(roundRepository.existsByFirstTitle_idAndSecondTitle_id(any(), any())).thenReturn(false);
        when(imdbGatewayService.getDetailsOfTitles(any(), any())).thenReturn(roundResponse);

        var roundServiceResponse = service.getPair("teste");

        assertEquals(roundResponse, roundServiceResponse);
    }


    @Test
    public void returnValidRoundResponseWhenTitlesWithSameRanking() throws IOException, InterruptedException, NotFinishedException, FinishedException, GatewayException, NotFoundException {
        var validTitlesGenre = Arrays.asList("/title/tt12345/", "/title/tt12345/", "/title/tt12346/");
        var invalidRoundResponse = getRoundResponse(8.0, 8.0);
        var validRoundResponse = getRoundResponse(8.0, 7.0);

        when(quizzRepository.findByUser_usernameAndFinished(eq("teste"), eq(false))).thenReturn(Optional.of(new Quizz()));
        when(roundRepository.existsByQuizz_UserAndFinished(any(), any())).thenReturn(false);

        when(imdbGatewayService.getTitlesByGenre(any())).thenReturn(new TitlesGenreResponse(validTitlesGenre));
        when(roundRepository.existsByFirstTitle_idAndSecondTitle_id(any(), any())).thenReturn(false);
        when(imdbGatewayService.getDetailsOfTitles(any(), any())).thenReturn(invalidRoundResponse).thenReturn(validRoundResponse);

        var roundServiceResponse = service.getPair("teste");

        assertEquals(validRoundResponse, roundServiceResponse);
    }

    @Test
    public void returnExceptionWhenUsernameNotFound() {
        when(userRepository.findByUsername(eq("teste"))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getPair("teste"));
    }

    @Test
    public void returnExceptionWhenNotFoundOpenedRound() {
        var user = new User();
        var validAnswerRequest = "tt12345";
        var roundAnswerRequest = new RoundAnswerRequest(validAnswerRequest);

        when(userRepository.findByUsername(eq("teste"))).thenReturn(Optional.of(user));
        when(roundRepository.findByQuizz_UserAndFinished(eq(user), eq(false))).thenReturn(Optional.empty());

        assertThrows(FinishedException.class, () -> service.saveAnswer(roundAnswerRequest, "teste"));
    }

    @Test
    public void returnRoundResponseInformingRightAnswer() throws NotFoundException, FinishedException {
        var user = new User();
        var validAnswerRequest = "tt12345";
        var invalidAnswerRequest = "tt12355";
        var roundAnswerRequest = new RoundAnswerRequest(validAnswerRequest);
        var quizz = new Quizz(user);
        var round = new Round(
                quizz,
                new Title(validAnswerRequest, validAnswerRequest, 8.0, 2020),
                new Title(invalidAnswerRequest, invalidAnswerRequest, 7.0, 2020)
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(roundRepository.findByQuizz_UserAndFinished(eq(user), eq(false))).thenReturn(Optional.of(round));
        when(roundRepository.findAllByQuizz(quizz)).thenReturn(Arrays.asList(round));
        when(quizzRepository.countByUser(user)).thenReturn(1);

        var roundResultResponse = service.saveAnswer(roundAnswerRequest, "teste");

        assertEquals(validAnswerRequest, roundResultResponse.getRightAnswer());
        assertEquals(100, roundResultResponse.getRound().getQuizz().getUser().getScore());
        assertTrue(roundResultResponse.getRound().getFinished());
        assertTrue(roundResultResponse.getRound().getAnsweredCorrectly());
        assertFalse(roundResultResponse.getRound().getQuizz().getFinished());
    }

    @Test
    public void returnRoundResponseInformingWrongAnswer() throws NotFoundException, FinishedException {
        var user = new User();
        var validAnswerRequest = "tt12345";
        var invalidAnswerRequest = "tt12355";
        var roundAnswerRequest = new RoundAnswerRequest(invalidAnswerRequest);
        var quizz = new Quizz(user);
        var round = new Round(
                quizz,
                new Title(validAnswerRequest, validAnswerRequest, 8.0, 2020),
                new Title(invalidAnswerRequest, invalidAnswerRequest, 7.0, 2020)
        );

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(roundRepository.findByQuizz_UserAndFinished(eq(user), eq(false))).thenReturn(Optional.of(round));
        when(roundRepository.findAllByQuizz(quizz)).thenReturn(Arrays.asList(round));
        when(quizzRepository.countByUser(user)).thenReturn(1);

        var roundResultResponse = service.saveAnswer(roundAnswerRequest, "teste");

        assertEquals(validAnswerRequest, roundResultResponse.getRightAnswer());
        assertEquals(0, roundResultResponse.getRound().getQuizz().getUser().getScore());
        assertEquals(1, roundResultResponse.getRound().getQuizz().getErrors());
        assertTrue(roundResultResponse.getRound().getFinished());
        assertFalse(roundResultResponse.getRound().getAnsweredCorrectly());
        assertFalse(roundResultResponse.getRound().getQuizz().getFinished());
    }

    private RoundResponse getRoundResponse(Double ratingFirstTitle, Double ratingSecondTitle) {
        return new RoundResponse(
                new TitlesResponse(
                        "tt12345",
                        "filme1",
                        ratingFirstTitle,
                        2020
                ),
                new TitlesResponse(
                        "tt12346",
                        "filme2",
                        ratingSecondTitle,
                        2020
                )
        );
    }
}
