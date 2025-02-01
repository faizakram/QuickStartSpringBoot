package com.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class CustomException  extends RuntimeException {

    private final String errorCode;      // Custom error code (e.g., "E1001")
    private final String errorMessage;   // Detailed error message
    private final HttpStatus httpStatus; // Associated HTTP status code

    /**
     * Constructor for CustomException.
     *
     * @param errorCode    The custom error code.
     * @param errorMessage The error message.
     * @param httpStatus   The HTTP status associated with the error.
     */
    public CustomException(String errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);  // Pass the message to the superclass (RuntimeException)
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
