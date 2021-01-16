package com.morlimoore.currencyconverterapi.controllers;

import com.morlimoore.currencyconverterapi.DTOs.CreateWalletDTO;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.payload.FixerApiResponse;
import com.morlimoore.currencyconverterapi.service.FixerApiService;
import com.morlimoore.currencyconverterapi.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.errorResponse;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private FixerApiService fixerApiService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ELITE')")
    public ResponseEntity<ApiResponse<String>> createWallet(@Valid @RequestBody CreateWalletDTO createWalletDTO,
                                                            BindingResult result) {
        if (result.hasErrors())
            return errorResponse(result.getFieldError().getDefaultMessage());
        if (!walletService.isCurrencySupported(createWalletDTO.getCurrency()))
            return errorResponse("Sorry, selected currency is not available, please select another.");
        return walletService.createWallet(createWalletDTO);
    }

//    @GetMapping("currencies")
//    public FixerApiResponse getAvailableCurrencies() {
//        return fixerApiService.getAvailableCurrencies();
//    }
}
