package com.morlimoore.currencyconverterapi.controllers;

import com.morlimoore.currencyconverterapi.DTOs.LoginRequestDTO;
import com.morlimoore.currencyconverterapi.DTOs.SignupRequestDTO;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.errorResponse;
import static com.morlimoore.currencyconverterapi.util.CreateResponse.createResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO,
									   BindingResult result) {
		if (result.hasErrors())
			return errorResponse(result.getFieldError().getDefaultMessage());
		return authService.authenticateUser(loginRequestDTO);
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO, BindingResult result) {
		if (result.hasErrors())
			return errorResponse(result.getFieldError().getDefaultMessage());
		else if (authService.ifUsernameExists(signupRequestDTO.getUsername()))
			return errorResponse("Username is already taken!");
		else if (authService.ifEmailExists(signupRequestDTO.getEmail()))
			return errorResponse("Email address is already taken!");

		// Create new user's account
		return authService.createUserAccount(signupRequestDTO);
	}
}
