package org.swe.business;

import org.swe.core.exceptions.InternalServerErrorException;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

import java.util.HashMap;
import java.util.Map;

public class VerifySessionServiceImpl implements VerifySessionService{
    private final Map<String, VerifySession> sessionData = new HashMap<>();


    public void addToSession(String key, VerifySession value) {
        sessionData.put(key, value);
    }

    public VerifySession getFromSession(String key) {
        return sessionData.get(key);
    }

    public void removeFromSession(String key) {
        sessionData.remove(key);
    }

    public boolean isInSession(String key) {
        return sessionData.containsKey(key);
    }

    public void clearSession() {
        sessionData.clear();
    }

    public void verifySession(String key, Integer ticketId) {
        VerifySession session = sessionData.get(key);
        if (session == null) {
            throw new InternalServerErrorException("Could not find session with key: " + key);
        }
        session.setTicketId(ticketId);
        session.setStatus(VerifySessionStatus.VERIFIED);
    }

    public void rejectSession(String key) {
        VerifySession session = sessionData.get(key);
        if (session == null) {
            throw new InternalServerErrorException("Could not find session with key: " + key);
        }
        session.setStatus(VerifySessionStatus.INVALID);
    }

    public void rejectSession(String key, Integer ticketId) {
        VerifySession session = sessionData.get(key);
        if (session == null) {
            throw new InternalServerErrorException("Could not find session with key: " + key);
        }
        session.setTicketId(ticketId);
        session.setStatus(VerifySessionStatus.INVALID);
    }
}
