package com.morlimoore.currencyconverterapi.util;

import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class CreateResponse {

    public static <T> ResponseEntity<ApiResponse<T>> createResponse(ApiResponse<T> response) {
        return new ResponseEntity<>(response, response.getStatus());
    }

    public static ResponseEntity<ApiResponse<String>> errorResponse(String message) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(BAD_REQUEST);
        response.setMessage(message);
        return createResponse(response);
    }

    public static ResponseEntity<ApiResponse<String>> successResponse(String message) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(OK);
        response.setMessage(message);
        return createResponse(response);
    }

}