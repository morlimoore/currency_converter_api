package com.morlimoore.currencyconverterapi.service;

import com.morlimoore.currencyconverterapi.DTOs.LoginRequestDTO;
import com.morlimoore.currencyconverterapi.DTOs.SignupRequestDTO;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.payload.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    Boolean ifUsernameExists(String username);

    Boolean ifEmailExists(String email);

    ResponseEntity<ApiResponse<String>> createUserAccount(SignupRequestDTO signupRequestDTO);

    ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(LoginRequestDTO loginRequestDTO);
}
