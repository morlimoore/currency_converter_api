package com.morlimoore.currencyconverterapi.util;

import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CreateResponse {

    public static <T> ResponseEntity<ApiResponse<T>> createResponse(ApiResponse<T> response) {
        return new ResponseEntity<>(response, response.getStatus());
    }

    public static ResponseEntity<ApiResponse<String>> bindingResultError(BindingResult result) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(BAD_REQUEST);
        response.setMessage(result.getFieldError().getDefaultMessage());
        return createResponse(response);
    }
}