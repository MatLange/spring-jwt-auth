package com.ml.restapi.controllers;

import com.ml.restapi.auth.JwtUtil;
import com.ml.restapi.model.User;
import com.ml.restapi.model.request.LoginReq;
import com.ml.restapi.model.response.ErrorRes;
import com.ml.restapi.model.response.LoginRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;


    private JwtUtil jwtUtil;
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    /* The @RequestBody annotation in Java indicates that the parameter loginReq should be bound to the body of the HTTP request. Spring will deserialize the request body JSON into a LoginReq object which can then be used in the method. So it is mapping the JSON request body onto a LoginReq Java object automatically. */
    public ResponseEntity login(@RequestBody LoginReq loginReq)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();
            User user = new User(email,"");
            String token = jwtUtil.createToken(user);
            LoginRes loginRes = new LoginRes(email,token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}

/*
 * This Java code is for an AuthController that handles user authentication and JWT token generation in a Spring Boot application.

Here is an explanation of what it is doing:

It is a controller with request mapping "/rest/auth" to handle authentication REST API requests

The login() method takes a LoginReq request body containing the user's email and password

It authenticates the user using Spring Security's AuthenticationManager

If authentication succeeds, it generates a JWT token for the user using the JwtUtil class

It returns a LoginRes response containing the user's email and the JWT token

If authentication fails with bad credentials, it returns a 40X error response

If any other exception happens during authentication, it returns a 40X error response

So in summary, this AuthController exposes a /login endpoint that allows users to submit credentials, authenticates them, generates a JWT token if valid credentials, and returns the token in the API response. It handles various error cases with 40X error responses.
*/