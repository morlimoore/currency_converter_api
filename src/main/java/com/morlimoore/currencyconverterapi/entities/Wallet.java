package com.morlimoore.currencyconverterapi.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "wallets")
@Getter
@Setter
public class Wallet extends BaseEntity {

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Wallet(String currency) {
        this.currency = currency;
    }
}