package com.letscode.moviesbattle.web.filter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserFilter extends OncePerRequestFilter {

    private static final String USERNAME_ATTRIBUTE = "USERNAME";
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var username = authentication.getPrincipal();

        if (authentication.getAuthorities().stream().anyMatch(role -> role.equals(ROLE_ANONYMOUS))) {
            filterChain.doFilter(request, response);
            return;
        }

        RequestContextHolder.getRequestAttributes().setAttribute(USERNAME_ATTRIBUTE, username, RequestAttributes.SCOPE_REQUEST);

        filterChain.doFilter(request, response);
        return;
    }
}
