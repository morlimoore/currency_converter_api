package com.morlimoore.currencyconverterapi.entities;

import com.morlimoore.currencyconverterapi.util.WalletEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "wallets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Wallet extends BaseEntity {

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletEnum type;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Wallet(String currency) {
        this.currency = currency;
    }
}