package com.letscode.moviesbattle.web.config;

import com.letscode.moviesbattle.web.filter.security.JWTAuthenticateFilter;
import com.letscode.moviesbattle.web.filter.security.JWTValidateFilter;
import com.letscode.moviesbattle.domain.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userService;

    public SecurityConfig(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var source = new UrlBasedCorsConfigurationSource();

        var corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        http.csrf().disable().cors().configurationSource(source).and()
                .authorizeRequests()
                .antMatchers("/login", "/user", "/swagger-ui.html", "/swagger-ui.html#", "/swagger-ui.html#/","/v2/api-docs", "/user/image",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticateFilter(authenticationManager(), getApplicationContext()))
                .addFilter(new JWTValidateFilter(authenticationManager(), getApplicationContext()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
