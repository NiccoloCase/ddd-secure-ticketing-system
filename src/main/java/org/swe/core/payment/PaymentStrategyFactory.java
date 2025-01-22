package org.swe.core.payment;

import org.swe.core.exceptions.BadRequestException;

import java.util.Objects;

public class PaymentStrategyFactory {
    public static PaymentStrategy getPaymentStrategy(String method) {
        if (Objects.equals(method, "CREDIT_CARD")) return new CreditCardPayment();
        if (Objects.equals(method, "APPLE_PAY")) return new ApplePayPayment();
        if(Objects.equals(method, "GOOGLE_PAY")) return new GooglePayPayment();

        else throw new BadRequestException("Invalid payment method. Please select a valid payment method.");
    }
}