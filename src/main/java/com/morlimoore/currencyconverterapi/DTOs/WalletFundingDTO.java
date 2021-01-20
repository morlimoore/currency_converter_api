package com.morlimoore.currencyconverterapi.DTOs;

import com.morlimoore.currencyconverterapi.entities.Wallet;
import lombok.Data;

@Data
public class WalletFundingDTO {

    private Wallet wallet;
    private Long amount;
}
