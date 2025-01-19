package org.swe.model;


public class PaymentStrategyFactory {
    public static PaymentStrategy getPaymentStrategy(PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.UNKNOWN) {
            return null;
        }
        if (paymentMethod == PaymentMethod.CREDIT_CARD) {
            return new CreditCardPayment();
        }
        if (paymentMethod == PaymentMethod.APPLE_PAY) {
            return new ApplePayPayment();
        }
        if (paymentMethod == PaymentMethod.GOOGLE_PAY) {
            return new GooglePayPayment();
        }
        return null;
    }
}