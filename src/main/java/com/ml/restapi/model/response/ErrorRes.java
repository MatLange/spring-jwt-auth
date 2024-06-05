package com.ml.restapi.model.response;

import org.springframework.http.HttpStatus;

public class ErrorRes {
    HttpStatus httpStatus;
    String message;

    public ErrorRes(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
/* This Java code defines a class called ErrorRes which represents an error response.

It has the following key components:

HttpStatus httpStatus - This field contains the HTTP status code associated with the error response, like 404 or 500.

String message - This field contains an error message describing the problem.

The constructor initializes the httpStatus and message fields.

There are getter and setter methods for both fields to retrieve their values or modify them.

So in summary, this ErrorRes class represents an error response by encapsulating an HTTP status code and error message. It provides a structured way to pass back error details to the client calling an API. An instance of this could be returned from a Spring controller method to communicate errors back to the client in a standard format.

Usage would look something like:

@ResponseStatus(HttpStatus.NOT_FOUND)
@ExceptionHandler(ResourceNotFoundException.class)
public ErrorRes handleNotFound(Exception ex) {
    return new ErrorRes(HttpStatus.NOT_FOUND, ex.getMessage());
}
This would send a 404 error response with the exception message.

 */