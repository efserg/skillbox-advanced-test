package com.skillbox.shop.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Transaction {

    private final double amount;
    private final LocalDateTime dateTime;
    private final String serviceCode;
    private String transactionId;

    public Transaction(double amount, String serviceCode) {
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.serviceCode = serviceCode;
    }
}
