package com.app.exception;

import com.app.dto.StandardResponse;
import com.app.utils.MessageConstant;
import com.app.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class RestExceptionHandler {

    private final ResponseUtil responseUtil;

    /**
     * Handles validation errors for method arguments.
     *
     * @param ex The MethodArgumentNotValidException to handle.
     * @return A ResponseEntity with a structured error response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return buildErrorResponse(MessageConstant.E1005, HttpStatus.BAD_REQUEST, ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, responseUtil::getMessage)));
    }

    /**
     * Handles custom ServiceException.
     *
     * @param ex The ServiceException to handle.
     * @return A ResponseEntity with an error response.
     */
    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<StandardResponse<Object>> handleServiceException(CustomRuntimeException ex) {
        log.error("Service exception: ", ex);
        return buildErrorResponse(ex.getResponseCode(), ex.getHttpStatus(), ex.getArgs());
    }

    /**
     * Handles generic RuntimeExceptions.
     *
     * @param ex The RuntimeException to handle.
     * @return A ResponseEntity with an error response.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception: ", ex);
        return buildErrorResponse(MessageConstant.E1000, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }


    /**
     * Utility method to build a standardized error response.
     *
     * @param responseCode The response code for the error.
     * @param details      The error details to include in the response.
     * @param httpStatus   The HTTP status for the response.
     * @return A ResponseEntity containing the StandardResponse.
     */
    private ResponseEntity<StandardResponse<Object>> buildErrorResponse(String responseCode, HttpStatus httpStatus, Object... details) {
        StandardResponse<Object> response = responseUtil.createErrorResponse(responseCode, details);
        return ResponseEntity.status(httpStatus).body(response);
    }
}
