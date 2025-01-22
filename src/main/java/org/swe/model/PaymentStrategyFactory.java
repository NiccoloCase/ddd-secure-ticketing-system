package org.swe.model;


public class PaymentStrategyFactory {
    public static PaymentStrategy getPaymentStrategy(PaymentMethod method) {
        if (method == PaymentMethod.UNKNOWN) {
            return null;
        }
        if (method == PaymentMethod.CREDIT_CARD) {
            return new CreditCardPayment();
        }
        if (method == PaymentMethod.APPLE_PAY) {
            return new ApplePayPayment();
        }
        if (method == PaymentMethod.GOOGLE_PAY) {
            return new GooglePayPayment();
        }
        // TODO ecc
        return null;
    }
}