package com.morlimoore.currencyconverterapi.service;

import com.morlimoore.currencyconverterapi.DTOs.CreateWalletDTO;
import com.morlimoore.currencyconverterapi.DTOs.FundWalletDTO;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface WalletService {

    ResponseEntity<ApiResponse<String>> createWallet(CreateWalletDTO createWalletDTO);

    ResponseEntity<ApiResponse<String>> fundWallet(User user, FundWalletDTO fundWalletDTO);

    Boolean isCurrencySupported(String currency);

    Wallet getUserMainWallet(Long userId);

    Boolean hasWalletInCurrency(String currency, Long userId);

    Wallet getWalletInCurrency(String currency, Long userId);

//    Wallet testQuery();
}
