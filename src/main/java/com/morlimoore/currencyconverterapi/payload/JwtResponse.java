package com.morlimoore.currencyconverterapi.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
	private Long id;
	private String username;
	private String email;
	private String role;
	private String type = "Bearer";
	private String token;


	public JwtResponse(String token, Long id, String username, String email, String role) {
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
	}
}
