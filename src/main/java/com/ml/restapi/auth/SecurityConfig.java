package com.ml.restapi.auth;

import com.ml.restapi.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.userDetailsService = customUserDetailsService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;

    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, NoOpPasswordEncoder noOpPasswordEncoder)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(noOpPasswordEncoder);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/rest/auth/login").permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtAuthorizationFilter,UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @SuppressWarnings("deprecation")
    @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

}
/*
This Java code configures Spring Security for the application. Here's an explanation of what it's doing:

It defines a SecurityConfig class annotated with @Configuration and @EnableWebSecurity to enable Spring Security.

It injects two beans into the config:

CustomUserDetailsService - a custom user details service that likely retrieves user details from a database
JwtAuthorizationFilter - a custom filter that handles JWT authentication
It defines an authenticationManager bean that uses the CustomUserDetailsService and a no-op password encoder for authentication.

It defines a SecurityFilterChain that:

Disables CSRF protection
Permits all requests to /rest/**
Requires authentication for any other requests
Configures session management to be stateless
Adds the JwtAuthorizationFilter filter for JWT handling
It also defines a NoOpPasswordEncoder bean to support plain text passwords.

In summary, it sets up:

Custom user details and authentication with plain text passwords
Stateless session management
JWT based authentication via a custom filter
Whitelisted public API endpoints
So it's configuring a stateless JWT-based authentication system with custom user details and password handling.

*/