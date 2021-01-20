package com.morlimoore.currencyconverterapi.util;

import com.morlimoore.currencyconverterapi.DTOs.WalletTransactionDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Getter
@Setter
public class AdminUtil {

    private Map<String, WalletTransactionDTO> Transactions = new HashMap<>();

    public String generateSerial() {
        return UUID.randomUUID().toString();
    }
}