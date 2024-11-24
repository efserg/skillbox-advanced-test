package com.skillbox;


import com.skillbox.shop.Cart;
import com.skillbox.shop.PaymentProcessor;
import com.skillbox.shop.PaymentService;
import com.skillbox.shop.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentProcessor paymentProcessorMock;
    private PaymentService paymentService;
    private Cart cartMock;

    @BeforeEach
    void setUp() {
        paymentProcessorMock = mock(PaymentProcessor.class);
        paymentService = new PaymentService(paymentProcessorMock);
        cartMock = mock(Cart.class);
    }

    @Test
    void testProcessPaymentFailed() {
        when(cartMock.calculateTotal()).thenReturn(100.0);
        boolean result = paymentService.processPayment(cartMock);
        assertFalse(result);
    }

    @Test
    void testTransactionIdUpdatedAfterProcessing() {
        when(cartMock.calculateTotal()).thenReturn(50.0);

        doAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setTransactionId("txn-123");
            return null;
        }).when(paymentProcessorMock).process(any(Transaction.class));

        boolean result = paymentService.processPayment(cartMock);

        assertTrue(result);
    }

    @Test
    void testProcessPaymentSuccess() {
        when(cartMock.calculateTotal()).thenReturn(100.0);

        doAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            transaction.setTransactionId("txn-123");
            return null;
        }).when(paymentProcessorMock).process(any(Transaction.class));

        boolean result = paymentService.processPayment(cartMock);

        assertTrue(result);

        // Проверка вызова метода process с корректной транзакцией
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(paymentProcessorMock).process(transactionCaptor.capture());

        Transaction capturedTransaction = transactionCaptor.getValue();
        assertEquals(100.0, capturedTransaction.getAmount());
        assertEquals(PaymentService.SERVICE_CODE, capturedTransaction.getServiceCode());
    }

    @Test
    void testProcessPaymentWithZeroAmount() {
        when(cartMock.calculateTotal()).thenReturn(0.0);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(cartMock));

        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    @Test
    void testProcessPaymentWithNegativeAmount() {
        when(cartMock.calculateTotal()).thenReturn(-10.0);

        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(cartMock));

        assertEquals("Amount must be greater than 0", exception.getMessage());
    }
}
