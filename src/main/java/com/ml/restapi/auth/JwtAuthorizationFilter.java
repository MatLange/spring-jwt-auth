package com.ml.restapi.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, ObjectMapper mapper) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();

        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null ) {
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("token : "+accessToken);
            Claims claims = jwtUtil.resolveClaims(request);

            if(claims != null & jwtUtil.validateClaims(claims)){
                String email = claims.getSubject();
                System.out.println("email : "+email);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email,"",new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }catch (Exception e){
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details",e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);

        }
        filterChain.doFilter(request, response);
    }
}
/* This code defines a JwtAuthorizationFilter class which extends the OncePerRequestFilter class in Spring. It is used to authorize requests by validating the JSON Web Token (JWT) sent with the request.

Here are the key things it does:

Injects dependencies on a JwtUtil class (for JWT handling) and ObjectMapper (for JSON serialization):
private final JwtUtil jwtUtil; 
private final ObjectMapper mapper;
In the doFilterInternal method, it extracts the JWT access token from the request:
String accessToken = jwtUtil.resolveToken(request);
It then validates the JWT claims and checks if it's valid:
Claims claims = jwtUtil.resolveClaims(request);

if(claims != null & jwtUtil.validateClaims(claims)){
  // token is valid
}
If valid, it extracts the username/email from the token claims and creates an authentication object:
String email = claims.getSubject();

Authentication authentication = new UsernamePasswordAuthenticationToken(email,"",new ArrayList<>()); 
The authentication object is set on the security context to authorize the user:
SecurityContextHolder.getContext().setAuthentication(authentication);
So in summary, it authorizes requests by verifying the JWT token, extracting the username from it, creating an authentication object and setting it as the authenticated principal on the security context.

 */


