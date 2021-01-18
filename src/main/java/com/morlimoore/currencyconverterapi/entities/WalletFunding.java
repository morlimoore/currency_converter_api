package com.morlimoore.currencyconverterapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "wallet_fundings")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WalletFunding extends BaseEntity {

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Boolean isApproved;

    @ManyToOne(cascade = CascadeType.ALL)
    private Wallet wallet;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}
