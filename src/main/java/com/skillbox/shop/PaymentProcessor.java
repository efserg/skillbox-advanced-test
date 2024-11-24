package com.skillbox.shop;

public interface PaymentProcessor {

    void process(Transaction transaction);

    boolean isPaymentSuccessful(String transactionId);
}
