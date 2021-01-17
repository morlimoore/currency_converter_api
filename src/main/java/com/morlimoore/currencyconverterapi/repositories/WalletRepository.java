package com.morlimoore.currencyconverterapi.repositories;

import com.morlimoore.currencyconverterapi.entities.Wallet;
import com.morlimoore.currencyconverterapi.util.WalletEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

//    @Query(value="SELECT * FROM wallets u WHERE type = 'MAIN' AND user_id = ?1", nativeQuery = true)
//    Wallet getUserMainWallet(Long userId);

    Wallet getWalletByTypeEqualsAndUserIdEquals(WalletEnum type, Long userId);

    Wallet getWalletByCurrencyEqualsAndUserIdEquals(String currency, Long userId);

    List<Wallet> getWalletsByUserIdEquals(Long userId);
}
