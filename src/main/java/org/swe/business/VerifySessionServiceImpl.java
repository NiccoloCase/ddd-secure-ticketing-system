package org.swe.business;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Jwt;
import org.swe.core.exceptions.InternalServerErrorException;
import org.swe.core.utils.JWTUtility;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

public class VerifySessionServiceImpl implements VerifySessionService {
    private final Map<String, VerifySession> sessionData = new HashMap<>();

    @Override
    public String addToSession(VerifySession value) {
        String key = generateNewSessionKey();
        sessionData.put(key, value);
        return key;
    }

    @Override
    public VerifySession getFromSession(String key) {
        return sessionData.get(key);
    }

    @Override
    public void removeFromSession(String key) {
        sessionData.remove(key);
    }

    @Override
    public boolean isInSession(String key) {
        return sessionData.containsKey(key);
    }

    @Override
    public void clearSession() {
        sessionData.clear();
    }

    @Override
    public void verifySession(String key, Integer ticketId) {
        VerifySession session = sessionData.get(key);
        if (session == null) {
            throw new InternalServerErrorException("Could not find session with key: " + key);
        }
        session.setTicketId(ticketId);
        session.setStatus(VerifySessionStatus.VERIFIED);
    }

    @Override
    public void rejectSession(String key) {
        VerifySession session = sessionData.get(key);
        if (session == null) {
            throw new InternalServerErrorException("Could not find session with key: " + key);
        }
        session.setStatus(VerifySessionStatus.INVALID);
    }

    @Override
    public void rejectSession(String key, Integer ticketId) {
        VerifySession session = sessionData.get(key);
        if (session == null) {
            throw new InternalServerErrorException("Could not find session with key: " + key);
        }
        session.setTicketId(ticketId);
        session.setStatus(VerifySessionStatus.INVALID);
    }



    private String generateVerificationCode(String sessionKey,  VerifySession session) {
        // The verification code is a JWT with the session key and staff id as claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("sessionKey", sessionKey);
        claims.put("staffId", session.getStaffId());

        return JWTUtility.generateToken(claims);

    }


    private String generateNewSessionKey(){
        String key = null;
        while (key == null || sessionData.containsKey(key)) {
            key = UUID.randomUUID().toString();
        }
        return key;
    }
}
