package com.ml.restapi.repositories;

import com.ml.restapi.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    public User findUserByEmail(String email){
        User user = new User(email,"123456");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        return user;
    }
}
/* This is a UserRepository Java class that is used to retrieve user information from a data source, likely a database. Here is an explanation of the key parts:

@Repository
public class UserRepository {
The @Repository annotation indicates that this class provides the data access layer. Spring will manage the life cycle of this bean.
    public User findUserByEmail(String email){
        User user = new User(email,"123456");
        user.setFirstName("FirstName"); 
        user.setLastName("LastName");
        return user;
    }
The findUserByEmail method accepts the user's email address and returns a User object with dummy hardcoded values.
It creates a new User instance, sets the email, password, first name and last name attributes.
This simulates querying a database by email and building a User object from the result to return.
In a real application, it would query the user table based on email and map the database result to a Java User object.
So in summary, this is a simple data access repository for retrieving user data, likely from a database. The implementation is dummy code for demonstration purposes.*/