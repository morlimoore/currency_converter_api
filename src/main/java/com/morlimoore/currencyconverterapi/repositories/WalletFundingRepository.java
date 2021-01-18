package com.morlimoore.currencyconverterapi.repositories;

import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.entities.WalletFunding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletFundingRepository extends JpaRepository<WalletFunding, Long> {

    WalletFunding findWalletFundingByUserEqualsAndIsApprovedFalse(User user);
}
