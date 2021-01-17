package com.morlimoore.currencyconverterapi.service;

import com.morlimoore.currencyconverterapi.DTOs.FundWalletDTO;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<ApiResponse<String>> fundUserWallet(Long userId, FundWalletDTO fundWalletDTO);
}