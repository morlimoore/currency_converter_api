package com.morlimoore.currencyconverterapi.exceptions;

import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.createResponse;

@ControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ApiResponse> handleAccessDeniedException(Exception ex) {
        ApiResponse response = new ApiResponse();
        response.setMessage("Sorry, you are not authorised to access this resource.");
        response.setStatus(HttpStatus.FORBIDDEN);
        return createResponse(response);
    }

    @ExceptionHandler({ IllegalStateException.class })
    public ResponseEntity<ApiResponse> handleIllegalStateException(Exception ex) {
        ApiResponse response = new ApiResponse();
        response.setMessage("Transaction failed. Please try again later.");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return createResponse(response);
    }

//    @ExceptionHandler()
//    public ResponseEntity<ApiResponse> handleHttpRequestFailureException(Exception ex) {
//        ApiResponse response = new ApiResponse();
//        response.setMessage(ex.getMessage());
//        response.setStatus(HttpStatus.BAD_REQUEST);
//        return createResponse(response);
//    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponse response = new ApiResponse();
        response.setMessage("Please provide a request body.");
        response.setStatus(HttpStatus.BAD_REQUEST);
        return createResponse(response);
    }
}