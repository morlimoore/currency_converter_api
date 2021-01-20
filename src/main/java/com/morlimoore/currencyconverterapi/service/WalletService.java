package com.morlimoore.currencyconverterapi.service;

import com.morlimoore.currencyconverterapi.DTOs.CreateWalletDTO;
import com.morlimoore.currencyconverterapi.DTOs.WalletTransactionDTO;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface WalletService {

    ResponseEntity<ApiResponse<String>> createWallet(User user, CreateWalletDTO createWalletDTO);

    ResponseEntity<ApiResponse<String>> fundWallet(User user, WalletTransactionDTO walletTransactionDTO);

    ResponseEntity<ApiResponse<String>> withdrawWallet(User user, WalletTransactionDTO walletTransactionDTO);

    Boolean isCurrencySupported(String currency);

    Wallet getUserMainWallet(Long userId);

    Boolean hasWalletInCurrency(String currency, Long userId);

    Wallet getWalletInCurrency(String currency, Long userId);

}
