package com.morlimoore.currencyconverterapi.DTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
public class FundWalletDTO {

    @NotBlank(message = "Please provide a currency code")
    @Size(max = 3)
    private String currency;

    @Positive(message = "Please enter a valid amount")
    private Long amount;
}
