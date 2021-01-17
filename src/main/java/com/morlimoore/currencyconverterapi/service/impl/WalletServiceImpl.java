package com.morlimoore.currencyconverterapi.service.impl;

import com.morlimoore.currencyconverterapi.DTOs.CreateWalletDTO;
import com.morlimoore.currencyconverterapi.DTOs.WalletTransactionDTO;
import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.exceptions.CustomException;
import com.morlimoore.currencyconverterapi.payload.ApiResponse;
import com.morlimoore.currencyconverterapi.repositories.WalletRepository;
import com.morlimoore.currencyconverterapi.service.CurrencyApiService;
import com.morlimoore.currencyconverterapi.service.WalletService;
import com.morlimoore.currencyconverterapi.util.AdminUtil;
import com.morlimoore.currencyconverterapi.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.morlimoore.currencyconverterapi.util.CreateResponse.successResponse;
import static com.morlimoore.currencyconverterapi.util.RoleEnum.*;
import static com.morlimoore.currencyconverterapi.util.WalletEnum.MAIN;
import static com.morlimoore.currencyconverterapi.util.WalletEnum.SECONDARY;

@Service
public class WalletServiceImpl implements WalletService {

    Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    private CurrencyApiService currencyApiService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private AdminUtil adminUtil;

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

    public ResponseEntity<ApiResponse<String>> fundWallet(User user, WalletTransactionDTO walletTransactionDTO) {
        String finalBalance = "";
        String responseMessage = "";

        String givenCurrency = walletTransactionDTO.getCurrency();

        if (user.getRole().equals(ROLE_NOOB)) {
            Wallet mainWallet = getUserMainWallet(user.getId());
            String mainCurrency = mainWallet.getCurrency();
            Long walletBalance = mainWallet.getAmount();

            //If currency of wallet funding is different from main currency
            if (!givenCurrency.equals(mainCurrency)) {
                Double rate = currencyApiService.getConversionRate(mainWallet.getCurrency(), walletTransactionDTO.getCurrency());
                logger.info("Convertion rate from " + givenCurrency + " to " + mainCurrency + " = " + rate);
                mainWallet.setAmount(walletBalance + (long)(walletTransactionDTO.getAmount() * rate));
                Wallet res = walletRepository.save(mainWallet);
                finalBalance = mainCurrency + res.getAmount();
            } else {
                mainWallet.setAmount(walletBalance + walletTransactionDTO.getAmount());
                Wallet res = walletRepository.save(mainWallet);
                finalBalance = mainCurrency + res.getAmount();
            }
            responseMessage = "Funding was successful. Your final balance is ";
        } else if (user.getRole().equals(ROLE_ELITE)) {
            //If a wallet exist for the user in that currency
            if (hasWalletInCurrency(givenCurrency, user.getId())) {
                Wallet wallet = getWalletInCurrency(givenCurrency, user.getId());
                wallet.setAmount(wallet.getAmount() + walletTransactionDTO.getAmount());
                Wallet res = walletRepository.save(wallet);
                finalBalance = givenCurrency + res.getAmount();
            } else {
                Wallet wallet = new Wallet(givenCurrency);
                wallet.setAmount(walletTransactionDTO.getAmount());
                wallet.setType(SECONDARY);
                wallet.setUser(user);
                Wallet res = walletRepository.save(wallet);
                finalBalance = givenCurrency + res.getAmount();
            }
            responseMessage = "Funding was successful. Your final balance is ";
        } else if (user.getRole().equals(ROLE_ADMIN)) {
            String serial = adminUtil.generateSerial();
            adminUtil.getTransactions().put(serial, walletTransactionDTO);
            responseMessage = "Dear Admin, visit: 'localhost:8080/api/v1/admin/wallet/fund/" + serial + "/userId'" +
                    " to fund user's account. Replace userId with actual user's Id.";
        }
        return successResponse(responseMessage + finalBalance);
    }

    @Override
    public ResponseEntity<ApiResponse<String>> withdrawWallet(User user, WalletTransactionDTO walletTransactionDTO) {
        String finalBalance = "";
        String responseMessage = "";

        Wallet mainWallet = getUserMainWallet(user.getId());
        String mainCurrency = mainWallet.getCurrency();
        String givenCurrency = walletTransactionDTO.getCurrency();

        if (user.getRole().equals(ROLE_NOOB)) {

            //If Noob user does not have wallet in that currency
            if (!mainCurrency.equals(givenCurrency)) {
                Double rate = currencyApiService.getConversionRate(mainWallet.getCurrency(), walletTransactionDTO.getCurrency());
                logger.info("Convertion rate from " + givenCurrency + " to " + mainCurrency + " = " + rate);
                Long amountToWithdraw = (long) (rate * walletTransactionDTO.getAmount());
                finalBalance = postWithdrawal(mainWallet, amountToWithdraw);
            } else {
                finalBalance = postWithdrawal(mainWallet, walletTransactionDTO.getAmount());
            }

        } else if (user.getRole().equals(ROLE_ELITE)) {
            if (hasWalletInCurrency(walletTransactionDTO.getCurrency(), user.getId())) {
                Wallet wallet = getWalletInCurrency(walletTransactionDTO.getCurrency(), user.getId());
                finalBalance = postWithdrawal(wallet, walletTransactionDTO.getAmount());
            } else {
                Double rate = currencyApiService.getConversionRate(mainWallet.getCurrency(), walletTransactionDTO.getCurrency());
                logger.info("Convertion rate from " + givenCurrency + " to " + mainCurrency + " = " + rate);
                Long amountToWithdraw = (long) (rate * walletTransactionDTO.getAmount());
                finalBalance = postWithdrawal(mainWallet, amountToWithdraw);

            }
        }
        responseMessage = "Withdrawal was successful. Your final balance is ";
        return successResponse(responseMessage + finalBalance);
    }

    private String postWithdrawal(Wallet wallet, Long amount) {
        String response = "";
        if (wallet.getAmount() > amount) {
            wallet.setAmount(wallet.getAmount() - amount);
            Wallet res = walletRepository.save(wallet);
            response = res.getCurrency() + res.getAmount();
        } else {
            throw new CustomException("Sorry, you have insufficient balance to withdraw", HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public Boolean isCurrencySupported(String currency) {
        return currencyApiService.getAvailableCurrencies()
                .getResults()
                .containsKey(currency.toUpperCase());
    }

    @Override
    public Wallet getUserMainWallet(Long userId) {
        return walletRepository.getWalletByTypeEqualsAndUserIdEquals(MAIN, userId);
    }

    @Override
    public Boolean hasWalletInCurrency(String currency, Long userId) {
        return walletRepository.getWalletsByUserIdEquals(userId)
                .stream()
                .filter(W -> W.getCurrency().equals(currency.toUpperCase()))
                .collect(Collectors.toList())
                .size() > 0;
    }

    @Override
    public Wallet getWalletInCurrency(String currency, Long userId) {
        return walletRepository.getWalletByCurrencyEqualsAndUserIdEquals(currency, userId);
    }



//    @Override
//    public Wallet testQuery() {
////        return walletRepository.getWalletByCurrencyEqualsAndUserIdEquals("USD", 2L);
////        return walletRepository.getUserMainWallet(1L);
//    }
}