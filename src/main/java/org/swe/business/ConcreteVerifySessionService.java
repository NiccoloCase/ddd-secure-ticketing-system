package org.swe.business;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.swe.core.exceptions.InternalServerErrorException;
import org.swe.core.utils.JWTUtility;
import org.swe.model.StartVerificationSessionResult;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

public class ConcreteVerifySessionService implements VerifySessionService {
    private final Map<String, VerifySession> sessionData = new HashMap<>();

    @Override
    public StartVerificationSessionResult addToSession(VerifySession value) {
    String key = generateNewSessionKey();
    sessionData.put(key, value);

    String verificationCode = generateVerificationCode(key, value);

    return new StartVerificationSessionResult(key, verificationCode);
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
    public void validateSession(String key) {
        VerifySession session = sessionData.get(key);
        if (session == null) {
            throw new InternalServerErrorException("Could not find session with key: " + key);
        }
        session.setStatus(VerifySessionStatus.VALIDATED);
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
    public String generateVerificationCode(String sessionKey,  VerifySession session) {
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
