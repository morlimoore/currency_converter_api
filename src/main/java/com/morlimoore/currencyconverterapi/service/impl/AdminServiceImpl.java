package com.morlimoore.currencyconverterapi.service.impl;

import com.morlimoore.currencyconverterapi.DTOs.AdminActionsDTO;
import com.morlimoore.currencyconverterapi.DTOs.WalletTransactionDTO;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.entities.WalletFunding;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.repositories.WalletFundingRepository;
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

    @Autowired
    private WalletFundingRepository walletFundingRepository;

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

    @Override
    public ResponseEntity<ApiResponse<String>> approveNoobFunding(Long userId) {
        User user = userService.findUserById(userId);
        WalletFunding walletFunding = walletFundingRepository.findWalletFundingByUserEqualsAndIsApprovedFalse(user);
        Wallet userWallet = walletFunding.getWallet();
        userWallet.setAmount(userWallet.getAmount() + walletFunding.getAmount());
        Wallet res = walletRepository.save(userWallet);
        walletFunding.setIsApproved(true);
        walletFundingRepository.save(walletFunding);
        return successResponse(user.getUsername() + "'s wallet funding has been approved. " +
                "Final balance = " + res.getCurrency() + res.getAmount());
    }

    @Override
    public ResponseEntity<ApiResponse<String>> manageUser(Long userId) {
        String response = "";
        User user = userService.findUserById(userId);
        int ordinal = user.getRole().ordinal();
        RoleEnum newRole;
        if (ordinal < 2) {
            if (ordinal == 0)
                newRole = RoleEnum.values()[ordinal + 1];
            else
                newRole = RoleEnum.values()[ordinal - 1];
            user.setRole(newRole);
            userRepository.save(user);
            response = user.getUsername() + " is now " + newRole + ".";
        } else
            return errorResponse(user.getUsername() + " is an admin.", BAD_REQUEST);

        return successResponse(response, OK);
    }
}