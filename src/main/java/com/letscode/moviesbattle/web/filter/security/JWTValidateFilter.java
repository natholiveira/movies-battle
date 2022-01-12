package com.letscode.moviesbattle.web.filter.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.letscode.moviesbattle.resources.properties.JwtTokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTValidateFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtTokenProperties jwtTokenProperties;

    public JWTValidateFilter(AuthenticationManager authenticationManager,  ApplicationContext ctx) {
        super(authenticationManager);
        this.jwtTokenProperties = ctx.getBean(JwtTokenProperties.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        var autorizationToken = request.getHeader(HEADER_AUTHORIZATION);

        if (autorizationToken == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!autorizationToken.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        var token = autorizationToken.replace(TOKEN_PREFIX, "");
        var authenticationToken = getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        var usuario = JWT.require(Algorithm.HMAC512(jwtTokenProperties.password))
                .build()
                .verify(token)
                .getSubject();

        return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
    }

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}
