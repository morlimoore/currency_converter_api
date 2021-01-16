package com.morlimoore.currencyconverterapi.service.impl;

import com.morlimoore.currencyconverterapi.DTOs.CreateWalletDTO;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.repositories.WalletRepository;
import com.morlimoore.currencyconverterapi.service.FixerApiService;
import com.morlimoore.currencyconverterapi.service.WalletService;
import com.morlimoore.currencyconverterapi.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.successResponse;
import static com.morlimoore.currencyconverterapi.util.WalletEnum.SECONDARY;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private FixerApiService fixerApiService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public ResponseEntity<ApiResponse<String>> createWallet(CreateWalletDTO createWalletDTO) {
        User user = authUtil.getAuthenticatedUser();
        Wallet wallet = new Wallet();
        wallet.setCurrency(createWalletDTO.getCurrency().toUpperCase());
        wallet.setType(SECONDARY);
        wallet.setAmount(0L);
        wallet.setUser(user);
        walletRepository.save(wallet);
        return successResponse("Wallet creation was successful");
    }

    @Override
    public Boolean isCurrencySupported(String currency) {
        return fixerApiService.getAvailableCurrencies()
                .getSymbols()
                .keySet()
                .contains(currency.toUpperCase());
    }
}
