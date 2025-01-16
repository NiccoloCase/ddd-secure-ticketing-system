package org.swe.model;


public interface PaymentStrategy {
    boolean processPayment(double amount);
}