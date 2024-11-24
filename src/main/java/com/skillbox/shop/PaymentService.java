package com.skillbox.shop;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentService {

    public static final String SERVICE_CODE = "SERVICE_CODE";

    private final PaymentProcessor paymentProcessor;

    public boolean processPayment(Cart cart) {
        double amount = cart.calculateTotal();
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Transaction transaction = new Transaction(amount, SERVICE_CODE);
        paymentProcessor.process(transaction);

        return transaction.getTransactionId() != null;
    }
}

