package org.swe.business;

import org.swe.model.VerifySession;

public interface VerifySessionService {

    String addToSession(VerifySession value);

    VerifySession getFromSession(String key);

    void removeFromSession(String key);

    boolean isInSession(String key);

    void clearSession();

    void validateSession(String key);

    void rejectSession(String key);

    String generateVerificationCode(String key, VerifySession session);

}
