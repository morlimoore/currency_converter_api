package com.morlimoore.currencyconverterapi.repositories;

import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.util.WalletEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

   Wallet getWalletByTypeEqualsAndUserIdEquals(WalletEnum type, Long userId);

    Wallet getWalletByCurrencyEqualsAndUserIdEquals(String currency, Long userId);

    List<Wallet> getWalletsByUserIdEquals(Long userId);
}
