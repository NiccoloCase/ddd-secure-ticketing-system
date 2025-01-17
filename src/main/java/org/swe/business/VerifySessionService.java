package org.swe.business;

import org.swe.model.VerifySession;

public interface VerifySessionService {

    void addToSession(String key, VerifySession value);

    VerifySession getFromSession(String key);

    void removeFromSession(String key);

    boolean isInSession(String key);

    void clearSession();

    void verifySession(String key, Integer ticketId);

    void rejectSession(String key);

    void rejectSession(String key, Integer ticketId);

}
