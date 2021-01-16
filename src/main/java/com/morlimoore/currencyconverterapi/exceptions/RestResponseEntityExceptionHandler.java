package com.morlimoore.currencyconverterapi.exceptions;

import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.createResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ApiResponse> handleAccessDeniedException(Exception ex) {
        ApiResponse response = new ApiResponse();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN);
        return createResponse(response);
    }
}