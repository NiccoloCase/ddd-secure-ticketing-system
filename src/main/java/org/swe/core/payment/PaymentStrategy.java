package org.swe.core.payment;


public interface PaymentStrategy {
    boolean processPayment(double amount);
}