package com.morlimoore.currencyconverterapi.DTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequestDTO {

	@NotBlank(message = "Username cannot be blank")
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank(message="Please enter a password")
	@Size(min = 6, max = 40)
	private String password;

}
