package org.swe.model;

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing Credit Card Payment of " + amount);
        return true;
    }
}

