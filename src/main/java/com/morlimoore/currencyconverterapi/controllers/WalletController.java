package com.morlimoore.currencyconverterapi.controllers;

import com.morlimoore.currencyconverterapi.DTOs.CreateWalletDTO;
import com.morlimoore.currencyconverterapi.DTOs.WalletTransactionDTO;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.service.WalletService;
import com.morlimoore.currencyconverterapi.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.errorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private AuthUtil authUtil;


    @PostMapping("/create")
    @PreAuthorize("hasRole('ELITE')")
    public ResponseEntity<ApiResponse<String>> createWallet(@Valid @RequestBody CreateWalletDTO createWalletDTO,
                                                            BindingResult result) {
        if (result.hasErrors())
            return errorResponse(result.getFieldError().getDefaultMessage(), BAD_REQUEST);
        if (!walletService.isCurrencySupported(createWalletDTO.getCurrency()))
            return errorResponse("Sorry, selected currency is not available, please select another.", BAD_REQUEST);
        return walletService.createWallet(createWalletDTO);
    }

    @PostMapping("/fund")
    public ResponseEntity<ApiResponse<String>> fundWallet(@Valid @RequestBody WalletTransactionDTO walletTransactionDTO,
                                                          BindingResult result) {
        if (result.hasErrors())
            return errorResponse(result.getFieldError().getDefaultMessage(), BAD_REQUEST);
        if (!walletService.isCurrencySupported(walletTransactionDTO.getCurrency()))
            return errorResponse("Sorry, selected currency is not available, please select another.", BAD_REQUEST);
        User user = authUtil.getAuthenticatedUser();
        return walletService.fundWallet(user, walletTransactionDTO);
    }

    @PostMapping("/withdraw")
    @PreAuthorize("!hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> withdrawWallet(@Valid @RequestBody WalletTransactionDTO walletTransactionDTO,
                                                              BindingResult result) {
        if (result.hasErrors())
            return errorResponse(result.getFieldError().getDefaultMessage(),BAD_REQUEST);
        User user = authUtil.getAuthenticatedUser();
        return walletService.withdrawWallet(user, walletTransactionDTO);
    }
}
