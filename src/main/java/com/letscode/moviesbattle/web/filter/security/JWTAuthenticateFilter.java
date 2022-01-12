package com.letscode.moviesbattle.web.filter.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.letscode.moviesbattle.domain.data.UserDetailsData;
import com.letscode.moviesbattle.domain.model.User;
import com.letscode.moviesbattle.resources.properties.JwtTokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

public class JWTAuthenticateFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtTokenProperties jwtTokenProperties;

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticateFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProperties = ctx.getBean(JwtTokenProperties.class);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            var user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>()
            ));
        } catch (IOException e) {
            throw new RuntimeException("failed to authenticate user", e);
        }
    }

    @Override
    @Bean
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        var userData = (UserDetailsData) authResult.getPrincipal();

        var token = JWT.create().
                withSubject(userData.getUsername())
                .withExpiresAt(Date.from(Instant.now().plus(jwtTokenProperties.expiration, ChronoUnit.SECONDS)))
                .sign(Algorithm.HMAC512(jwtTokenProperties.password));

        response.getWriter().write(token);
        response.getWriter().flush();
    }
}
