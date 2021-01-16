package com.morlimoore.currencyconverterapi.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequestDTO {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20)
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email(message="Please enter a valid Email address")
    private String email;
    
    @NotBlank(message="Please enter a password")
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank(message = "Please provide a main currency")
    private String mainCurrency;
}
