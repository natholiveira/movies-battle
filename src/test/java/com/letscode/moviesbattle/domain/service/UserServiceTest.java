package com.letscode.moviesbattle.domain.service;

import com.letscode.moviesbattle.domain.exception.DuplicatedUserException;
import com.letscode.moviesbattle.domain.model.User;
import com.letscode.moviesbattle.domain.repository.UserRepository;
import com.letscode.moviesbattle.web.request.UserRequest;
import com.letscode.moviesbattle.domain.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Test
    public void validDuplicatedUsernameWhenUserWithEmailAlreadyExists() {
        var userRequest = new UserRequest("teste","teste", "passwordTeste");

        var user = new User(new UserRequest("teste","teste", "passwordTeste"), "passwordTeste");

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(DuplicatedUserException.class, () -> userService.create(userRequest));
    }

    @Test
    public void createUserWhenIsValid() throws DuplicatedUserException {
        var userRequest = new UserRequest("teste","teste", "passwordTeste");

        userService.create(userRequest);

        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(userRequest.getPassword());
    }

    @Test
    public void verifyGetRanking() {
        userService.getRanking();

        verify(userRepository, times(1)).findAllOderByRankingPositionDesc();
    }
}
