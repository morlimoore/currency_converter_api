package com.morlimoore.currencyconverterapi.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * CustomException
 */
@Data
public class CustomException extends RuntimeException {
    /**
     * For serialization: if any changes is made to this class, update the
     * serialversionID
     */
    private static final long serialVersionUID = 1L;

    private String message;
    private HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
