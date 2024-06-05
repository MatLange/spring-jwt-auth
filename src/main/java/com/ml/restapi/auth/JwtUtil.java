package com.ml.restapi.auth;

import com.ml.restapi.model.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    //@Autowired
    //private Environment environment;

    private final String secret_key = "NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3NllmZHptNVVrNG9RRUs3Nl";
;
    private long accessTokenValidity = 60*60*1000;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil(){
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    public String createToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("firstName",user.getFirstName());
        claims.put("lastName",user.getLastName());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }


}
/* This Java code is for a JWT (JSON Web Token) utility class to handle JWT token creation, parsing, and validation.

Some key things this code does:

Defines some constants like:

secret_key - the secret key used to sign the JWT
TOKEN_HEADER - the HTTP header name that will contain the JWT
TOKEN_PREFIX - the prefix before the JWT that identifies it
createToken() method

Generates a signed JWT token containing user claims and an expiration
Uses the HS256 signing algorithm and the defined secret key
parseJwtClaims() method

Parses and returns the JWT claims given a JWT token
resolveClaims() method

Gets the JWT token from the HTTP request
Calls parseJwtClaims() to parse claims
Catches expired or invalid tokens
resolveToken() method

Extracts the JWT token from the HTTP header
validateClaims() method

Checks if the expiration of the claims is in the future
Other helper methods like:

getEmail() - Get email from claims
getRoles() - Get roles from claims
So in summary, it provides reusable methods to handle JWT creation, parsing, validation and extraction in a Spring application. The key purpose is offloading JWT handling from the application code into this reusable utility class. */