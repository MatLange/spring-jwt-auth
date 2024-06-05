# Spring Boot app to expose a simple REST API secured with Spring security using JWT authentication

> # Spring Boot app to expose a simple REST API secured with Spring security using JWT authentication

# This spring boot app uses Maven for building

# How to call it (via POSTMAN i.e.):

Send a post request to the route /rest/auth/login containing username and password in the request body:
{
    "email":"ex@example.com",
    "password":"123456"
}

The request will return a JWT token.

This tokenreceived JWT token can then be send via a GET request (in the authorization tab under type: Bearer token) to the route /rest/home, there the authentication is checked by validating the JWT token.
If successful the response "Hello world" is then send back to the client.