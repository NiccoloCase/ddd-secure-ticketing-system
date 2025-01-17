package org.swe.business;

import org.swe.core.exceptions.InternalServerErrorException;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VerifySessionServiceImpl implements VerifySessionService {
    private final Map<String, VerifySession> sessionData = new HashMap<>();


    public String addToSession(VerifySession value) {
        String key = generateNewSessionKey();
        sessionData.put(key, value);
        return key;
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

    private String generateNewSessionKey(){
        String key = null;
        while (key == null || sessionData.containsKey(key)) {
            key = UUID.randomUUID().toString();
        }
        return key;
    }
}
