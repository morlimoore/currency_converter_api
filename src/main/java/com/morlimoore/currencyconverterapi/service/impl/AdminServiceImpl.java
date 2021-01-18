package com.morlimoore.currencyconverterapi.service.impl;

import com.morlimoore.currencyconverterapi.DTOs.AdminActionsDTO;
import com.morlimoore.currencyconverterapi.DTOs.WalletTransactionDTO;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.repositories.WalletRepository;
import com.morlimoore.currencyconverterapi.service.AdminService;
import com.morlimoore.currencyconverterapi.service.CurrencyApiService;
import com.morlimoore.currencyconverterapi.service.UserService;
import com.morlimoore.currencyconverterapi.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.*;

@Service
public class AdminServiceImpl implements AdminService {

    Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CurrencyApiService currencyApiService;

    @Override
    public ResponseEntity<ApiResponse<String>> fundUserWallet(Long userId, WalletTransactionDTO walletTransactionDTO) {
        User user = userService.findUserById(userId);
        return walletService.fundWallet(user, walletTransactionDTO);
    }

    @Override
    public ResponseEntity<ApiResponse<String>> changeMainCurrency(Long userId, AdminActionsDTO adminActionsDTO) {
        User user = userService.findUserById(userId);
        Wallet mainWallet = walletService.getUserMainWallet(user.getId());
        String existingCurrency = mainWallet.getCurrency();
        String finalCurrency = adminActionsDTO.getTargetCurrency();
        if (finalCurrency.equals(existingCurrency))
            return errorResponse("Currency already user's main");
        Double rate = currencyApiService.getConversionRate(finalCurrency, existingCurrency);
        logger.info("Conversion rate from " + existingCurrency + " to " + finalCurrency + " = " + rate);
        mainWallet.setAmount((long)(rate * mainWallet.getAmount()));
        mainWallet.setCurrency(finalCurrency);
        walletRepository.save(mainWallet);
        return successResponse("Main currency changed successfully");
    }


}