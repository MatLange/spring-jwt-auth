package com.ml.restapi.services;

import com.ml.restapi.model.User;
import com.ml.restapi.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(roles.toArray(new String[0]))
                        .build();
        return userDetails;
    }
}
/* This code defines a custom UserDetailsService implementation called CustomUserDetailsService. Here is an explanation of what it does:

It is annotated with @Service so that it will be picked up by Spring's component scanning and registered as a bean.

It has a dependency on a UserRepository bean which provides access to user data.

It implements the loadUserByUsername method from the UserDetailsService interface. This method is called by Spring Security to fetch user details by username on authentication requests.

Inside the method:

It looks up the User object by email from the UserRepository

It builds a UserDetails object containing:

The user's email as the username
The user's password
A "USER" role
Gets returned as the authentication principal
So in summary, this CustomUserDetailsService loads the user information from a database using a UserRepository and builds a Spring Security UserDetails object that can be used for authentication and authorization.

The key thing it provides is a bridge between the user data store and Spring Security.
*/