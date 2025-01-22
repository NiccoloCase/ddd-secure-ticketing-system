package org.swe.core.payment;

public class GooglePayPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing Google Pay Payment of " + amount);
        return true;
    }
}