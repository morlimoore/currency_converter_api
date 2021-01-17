package com.morlimoore.currencyconverterapi.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
@Data
public class Transaction extends BaseEntity {

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Boolean isApproved;

    @ManyToOne
    private User user;

    @ManyToOne
    private Wallet wallet;
}