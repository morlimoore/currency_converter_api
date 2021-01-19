package com.morlimoore.currencyconverterapi.exceptions;

import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import io.netty.resolver.dns.DnsNameResolverTimeoutException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.NoRouteToHostException;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.*;

@ControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(Exception ex) {
        return errorResponse("Sorry, you are not authorised to access this resource.",
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ IllegalStateException.class })
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(Exception ex) {
        return errorResponse("Transaction failed. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ CustomException.class })
    public ResponseEntity<ApiResponse<String>> handleCustomException(Exception ex) {
       return errorResponse(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ NoRouteToHostException.class })
    public ResponseEntity<ApiResponse<String>> handleNoRouteToHostException(Exception ex) {
        return errorResponse("Error reaching server. Please check your internet connection",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ DnsNameResolverTimeoutException.class })
    public ResponseEntity<ApiResponse<String>> handleDnsNameResolverTimeoutException(Exception ex) {
        return errorResponse("There was an error reaching the server, please try again.",
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({ BadCredentialsException.class })
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(Exception ex) {
        return errorResponse("Username or password is invalid. Check and try again",
                HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler()
//    public ResponseEntity<ApiResponse> handleHttpRequestFailureException(Exception ex) {
//        ApiResponse response = new ApiResponse();
//        response.setMessage(ex.getMessage());
//        response.setStatus(HttpStatus.BAD_REQUEST);
//        return createResponse(response);
//    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ApiResponse response = new ApiResponse();
        response.setMessage("ERROR");
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setResult("Please provide a request body.");
        return createResponse(response);
    }
}