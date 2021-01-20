package com.morlimoore.currencyconverterapi.controllers;

import com.morlimoore.currencyconverterapi.DTOs.LoginRequestDTO;
import com.morlimoore.currencyconverterapi.DTOs.SignupRequestDTO;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.service.AuthService;
import com.morlimoore.currencyconverterapi.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.errorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private WalletService walletService;

	@PostMapping("/signin")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO,
									   BindingResult result) {
		if (result.hasErrors())
			return errorResponse(result.getFieldError().getDefaultMessage(), BAD_REQUEST);
		return authService.authenticateUser(loginRequestDTO);
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<String>> registerUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO, BindingResult result) {
		if (result.hasErrors())
			return errorResponse(result.getFieldError().getDefaultMessage(), BAD_REQUEST);
		else if (authService.ifUsernameExists(signupRequestDTO.getUsername()))
			return errorResponse("Username is already taken!", BAD_REQUEST);
		else if (authService.ifEmailExists(signupRequestDTO.getEmail()))
			return errorResponse("Email address is already taken!", BAD_REQUEST);
		else if (!walletService.isCurrencySupported(signupRequestDTO.getMainCurrency()))
			return errorResponse("Sorry, selected currency is not available, please select another.", BAD_REQUEST);

		// Create new user's account
		return authService.createUserAccount(signupRequestDTO);
	}
}
