package org.swe.model;

public class StartVerificationSessionResult {
    private final String key;
    private final String verificationCode;

    public StartVerificationSessionResult(String key, String verificationCode) {
        this.key = key;
        this.verificationCode = verificationCode;
    }

    public String getKey() {
        return key;
    }

    public String getVerificationCode() {
        return verificationCode;
    }
}