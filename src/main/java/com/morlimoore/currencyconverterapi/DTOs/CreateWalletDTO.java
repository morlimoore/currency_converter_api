package com.morlimoore.currencyconverterapi.DTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateWalletDTO {

    @NotBlank(message = "Please provide a currency code")
    @Size(max = 3)
    private String currency;
}
