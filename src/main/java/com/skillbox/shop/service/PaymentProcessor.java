package com.skillbox.shop.service;

import com.skillbox.shop.model.Transaction;

public interface PaymentProcessor {

    void process(Transaction transaction);

}
