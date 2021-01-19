package com.morlimoore.currencyconverterapi.util;

import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CreateResponse {

    public static <T> ResponseEntity<ApiResponse<T>> createResponse(ApiResponse<T> response) {
        return new ResponseEntity<>(response, response.getStatus());
    }

    public static ResponseEntity<ApiResponse<String>> errorResponse(String message, HttpStatus status) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        return createResponse(response);
    }

    public static ResponseEntity<ApiResponse<String>> successResponse(String message, HttpStatus status) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        return createResponse(response);
    }

    public static ResponseEntity<ApiResponse<String>> exceptionResponse(String message, HttpStatus status) {
        ApiResponse<String> response = new ApiResponse();
        response.setMessage(message);
        response.setStatus(status);
        return createResponse(response);
    }

}