package com.morlimoore.currencyconverterapi.payload;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private LocalDateTime time = LocalDateTime.now();
    private List<T> result;
}