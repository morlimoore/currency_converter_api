package com.morlimoore.currencyconverterapi.controllers;

import com.morlimoore.currencyconverterapi.DTOs.WalletTransactionDTO;
import com.morlimoore.currencyconverterapi.exceptions.CustomException;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.service.AdminService;
import com.morlimoore.currencyconverterapi.util.AdminUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    AdminUtil adminUtil;

    @PostMapping("/wallet/fund/{serial}/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> fundUserWallet(@PathVariable("serial") String serial,
                                                              @PathVariable("userId") Long userId) {
        Optional<WalletTransactionDTO> optional = Optional.ofNullable(adminUtil.getTransactions().get(serial));
        WalletTransactionDTO walletTransactionDTO = optional.orElseThrow(
                () -> new CustomException("URI is invalid, please redo the process.", HttpStatus.BAD_REQUEST));
        adminUtil.getTransactions().remove(serial);
        return adminService.fundUserWallet(userId, walletTransactionDTO);
    }
}
