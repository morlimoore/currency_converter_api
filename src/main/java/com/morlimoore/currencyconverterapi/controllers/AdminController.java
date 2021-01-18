package com.morlimoore.currencyconverterapi.controllers;

import com.morlimoore.currencyconverterapi.DTOs.AdminActionsDTO;
import com.morlimoore.currencyconverterapi.DTOs.WalletTransactionDTO;
import com.morlimoore.currencyconverterapi.exceptions.CustomException;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.service.AdminService;
import com.morlimoore.currencyconverterapi.service.WalletService;
import com.morlimoore.currencyconverterapi.util.AdminUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.errorResponse;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    WalletService walletService;

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

    @PostMapping("/wallet/currency-change/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> changeMainCurrency(@PathVariable("userId") Long userId,
                                                                  @RequestBody AdminActionsDTO adminActionsDTO) {
        if (!walletService.isCurrencySupported(adminActionsDTO.getTargetCurrency()))
            return errorResponse("Sorry, selected currency is not available, please select another.");
        return adminService.changeMainCurrency(userId, adminActionsDTO);
    }
}
