package com.letscode.moviesbattle.domain.service;

import com.letscode.moviesbattle.domain.exception.FinishedException;
import com.letscode.moviesbattle.domain.exception.NotFinishedException;
import com.letscode.moviesbattle.domain.exception.NotFoundException;
import com.letscode.moviesbattle.domain.model.Quizz;
import com.letscode.moviesbattle.domain.model.User;
import com.letscode.moviesbattle.resources.properties.ScoreProperties;
import com.letscode.moviesbattle.domain.repository.QuizzRepository;
import com.letscode.moviesbattle.domain.repository.RoundRepository;
import com.letscode.moviesbattle.domain.repository.UserRepository;
import com.letscode.moviesbattle.web.request.UserRequest;
import com.letscode.moviesbattle.domain.service.impl.QuizzServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class QuizzServiceTest {

    @Mock
    QuizzRepository quizzRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RoundRepository roundRepository;

    @Mock
    ScoreProperties scoreProperties;

    @InjectMocks
    QuizzServiceImpl quizzService;

    @Test
    public void returnExceptionWhenNotFoundUserInStart() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> quizzService.start(any()));
    }

    @Test
    public void returnExceptionWhenFoundUnfinishedQuizzInStart() {
        var user = new User(new UserRequest("teste","teste", "passwordTeste"), "passwordTeste");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(quizzRepository.findByUser_usernameAndFinished(any(), any())).thenReturn(Optional.of(new Quizz()));
        when(roundRepository.existsByQuizz_UserAndFinished(any(), any())).thenReturn(true);

        assertThrows(NotFinishedException.class, () -> quizzService.start(any()));
    }

    @Test
    public void returnExceptionWhenFoundUnfinishedRoundInStart() {
        var user = new User(new UserRequest("teste","teste", "passwordTeste"), "passwordTeste");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(quizzRepository.existsByUserAndFinished(any(), any())).thenReturn(false);
        when(roundRepository.existsByQuizz_UserAndFinished(any(), any())).thenReturn(true);

        assertThrows(NotFinishedException.class, () -> quizzService.start(any()));
    }

    @Test
    public void createQuizzWhenItIsValid() throws NotFoundException, NotFinishedException {
        var user = new User(new UserRequest("teste","teste", "passwordTeste"), "passwordTeste");
        var quizz = new Quizz(user);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(quizzRepository.findByUser_usernameAndFinished(any(), any())).thenReturn(Optional.empty());
        when(roundRepository.existsByQuizz_UserAndFinished(any(), any())).thenReturn(false);
        when(quizzRepository.save(any())).thenReturn(quizz);

        var quizzResponse = quizzService.start(user.getUsername());
        verify(quizzRepository, times(1)).save(any());

        assertEquals(0, quizzResponse.getCurrentRankingPosition());
        assertEquals(0, quizzResponse.getCurrentScore());
        assertEquals(0, quizzResponse.getErrors());
        assertFalse(quizzResponse.getFinished());
        assertNotNull(quizzResponse.getId());
    }

    @Test
    public void returnExceptionWhenNotFoundUserInFinished() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> quizzService.finish(any()));
    }

    @Test
    public void returnExceptionWhenFoundUnfinishedQuizzInFinished() {
        var user = new User(new UserRequest("teste","teste", "passwordTeste"), "passwordTeste");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(quizzRepository.findByUser_usernameAndFinished(any(), any())).thenReturn(Optional.empty());

        assertThrows(FinishedException.class, () -> quizzService.finish(any()));
    }

    @Test
    public void returnQuizzResponseWhithAverageRanking() throws NotFoundException, FinishedException {
        var user = new User(new UserRequest("teste","teste", "passwordTeste"), "passwordTeste");
        var user4 = getUSer(4L);
        var user5 = getUSer(5L);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(quizzRepository.findByUser_usernameAndFinished(any(), any())).thenReturn(Optional.of(new Quizz()));
        when(userRepository.count()).thenReturn(5L);
        when(userRepository.findAllByScoreLessThanOderByDesc(any())).thenReturn(Arrays.asList(user4, user5));

        var quizzResponse = quizzService.finish(user.getUsername());

        assertEquals(4, quizzResponse.getCurrentRankingPosition());
        assertEquals(user.getScore(), quizzResponse.getCurrentScore());
        assertEquals(0, quizzResponse.getErrors());
        assertTrue(quizzResponse.getFinished());
    }

    @Test
    public void returnQuizzResponseWhithLastPositionRanking() throws NotFoundException, FinishedException {
        var user = new User(new UserRequest("teste","teste", "passwordTeste"), "passwordTeste");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(quizzRepository.findByUser_usernameAndFinished(any(), any())).thenReturn(Optional.of(new Quizz()));
        when(userRepository.count()).thenReturn(5L);
        when(userRepository.findAllByScoreLessThanOderByDesc(any())).thenReturn(new ArrayList<>());

        var quizzResponse = quizzService.finish(user.getUsername());

        assertEquals(6, quizzResponse.getCurrentRankingPosition());
        assertEquals(user.getScore(), quizzResponse.getCurrentScore());
        assertEquals(0, quizzResponse.getErrors());
        assertTrue(quizzResponse.getFinished());
    }

    @Test
    public void returnQuizzResponseWhithFirstPositionRanking() throws NotFoundException, FinishedException {
        var user = new User(new UserRequest("teste","teste", "passwordTeste"), "passwordTeste");

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(quizzRepository.findByUser_usernameAndFinished(any(), any())).thenReturn(Optional.of(new Quizz()));
        when(userRepository.count()).thenReturn(1L);
        when(userRepository.findAllByScoreLessThanOderByDesc(any())).thenReturn(new ArrayList<>());

        var quizzResponse = quizzService.finish(user.getUsername());

        assertEquals(1, quizzResponse.getCurrentRankingPosition());
        assertEquals(user.getScore(), quizzResponse.getCurrentScore());
        assertEquals(0, quizzResponse.getErrors());
        assertTrue(quizzResponse.getFinished());
    }

    private User getUSer(Long rankingPosition) {
        return new User(
                "id",
                OffsetDateTime.now(ZoneOffset.UTC),
                OffsetDateTime.now(ZoneOffset.UTC),
                "teste",
                "teste",
                "passwordTeste",
                10L,
                rankingPosition
        );
    }
}
