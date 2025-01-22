package org.swe.core.payment;

public class ApplePayPayment implements PaymentStrategy{
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing Apple Pay Payment of " + amount);
        return true;
    }
}
