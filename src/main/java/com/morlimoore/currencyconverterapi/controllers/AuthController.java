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

import static com.morlimoore.currencyconverterapi.util.CreateResponse.bindingResultError;
import static com.morlimoore.currencyconverterapi.util.CreateResponse.createResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthService authService;

	@PostMapping("/signin")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO, BindingResult result) {
		if (result.hasErrors())
			return bindingResultError(result);
		return authService.authenticateUser(loginRequestDTO);
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO, BindingResult result) {
		if (result.hasErrors())
			return bindingResultError(result);
		else if (authService.ifUsernameExists(signupRequestDTO.getUsername())) {
			ApiResponse<String> response = new ApiResponse<>();
			response.setStatus(BAD_REQUEST);
			response.setMessage("Username is already taken!");
			return createResponse(response);
		} else if (authService.ifEmailExists(signupRequestDTO.getEmail())) {
			ApiResponse<String> response = new ApiResponse<>();
			response.setStatus(BAD_REQUEST);
			response.setMessage("Email address is already taken!");
			return createResponse(response);
		}
		// Create new user's account
		return authService.createUserAccount(signupRequestDTO);
	}
}
