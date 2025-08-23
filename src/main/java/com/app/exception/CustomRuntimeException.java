package com.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomRuntimeException extends RuntimeException {

    private final String responseCode;       // Unique error code
    private final HttpStatus httpStatus;    // HTTP status associated with the exception
    private final Object[] args;

    /**
     * Constructor for CustomRuntimeException.
     *
     * @param responseCode The unique error code.
     * @param httpStatus   The HTTP status associated with the exception.
     * @param args         Arguments for dynamic message formatting.
     */
    public CustomRuntimeException(String responseCode, HttpStatus httpStatus, Object... args) {
        super(responseCode); // Use responseCode as the default message for the exception
        this.responseCode = responseCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }
}
