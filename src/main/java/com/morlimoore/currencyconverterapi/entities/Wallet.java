package com.morlimoore.currencyconverterapi.entities;

import com.morlimoore.currencyconverterapi.util.WalletEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "wallets")
@NoArgsConstructor
@AllArgsConstructor
@Data
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