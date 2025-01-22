package org.swe.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.swe.core.exceptions.InternalServerErrorException;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

class ConcreteVerifySessionServiceTest {

    private ConcreteVerifySessionService service;

    @BeforeEach
    void setUp() {
        service = new ConcreteVerifySessionService();
    }

    @Test
    void testAddToSession() {
        VerifySession session = new VerifySession(1, 1001);
        String key = service.addToSession(session).getKey();

        VerifySession retrievedSession = service.getFromSession(key);
        assertNotNull(retrievedSession, "Session should be retrieved successfully");
        assertEquals(retrievedSession.getStatus(), VerifySessionStatus.WAITING_FOR_GUEST, "The status should be WAITING_FOR_GUEST");
        assertEquals(session, retrievedSession, "The added session and retrieved session should be the same");
    }

    @Test
    void testUniqueSessionKeys() {
        VerifySession session1 = new VerifySession(1, 1001);
        VerifySession session2 = new VerifySession(2, 1002);
        String key1 = service.addToSession(session1).getKey();
        String key2 = service.addToSession(session2).getKey();

        assertNotEquals(key1, key2, "The session keys should be unique");
    }


    @Test
    void testGetFromSessionWhenNotExists() {
        VerifySession retrievedSession = service.getFromSession("nonExistingSession");
        assertNull(retrievedSession, "The session should return null if it doesn't exist");
    }

    @Test
    void testRemoveFromSession() {
        VerifySession session = new VerifySession(1, 1001);
        String key = service.addToSession(session).getKey();
        service.removeFromSession(key);

        VerifySession retrievedSession = service.getFromSession(key);
        assertNull(retrievedSession, "The session should be removed and not retrievable");
    }

    @Test
    void testIsInSession() {
        VerifySession session = new VerifySession(1, 1001);
        String key = service.addToSession(session).getKey();

        assertTrue(service.isInSession(key), "The session should exist in the session data");
        assertFalse(service.isInSession("nonExistingSession"), "The session should not exist in the session data");
    }

    @Test
    void testClearSession() {
        VerifySession session1 = new VerifySession(1, 1001);
        VerifySession session2 = new VerifySession(2, 1002);
        String key1 = service.addToSession(session1).getKey();
        String key2 = service.addToSession(session2).getKey();

        service.clearSession();

        assertFalse(service.isInSession(key1), "Session 1 should be cleared");
        assertFalse(service.isInSession(key2), "Session 2 should be cleared");
    }

    @Test
    void testValidateSession() {
        VerifySession session = new VerifySession(1, 1001);
        String key = service.addToSession( session).getKey();

        service.validateSession(key);

        VerifySession updatedSession = service.getFromSession(key);
        assertNotNull(updatedSession, "Session should exist");
        assertEquals(VerifySessionStatus.VALIDATED, updatedSession.getStatus(), "Status should be VERIFIED");
    }

    @Test
    void testVerifySessionThrowsExceptionWhenSessionNotFound() {
        assertThrows(InternalServerErrorException.class, () -> {
            service.validateSession("nonExistingSession");
        });
    }

    @Test
    void testRejectSession() {
        VerifySession session = new VerifySession(1, 1001);
        String key = service.addToSession(session).getKey();

        service.rejectSession(key);

        VerifySession updatedSession = service.getFromSession(key);
        assertNotNull(updatedSession, "Session should exist");
        assertEquals(VerifySessionStatus.INVALID, updatedSession.getStatus(), "Status should be INVALID");
    }

    @Test
    void testRejectSessionThrowsExceptionWhenSessionNotFound() {
        assertThrows(InternalServerErrorException.class, () -> {
            service.rejectSession("nonExistingSession");
        });
    }

    @Test
    void testRejectSessionWithTicketId() {
        VerifySession session = new VerifySession(1, 1001);
        String key = service.addToSession(session).getKey();
        service.rejectSession(key);
        VerifySession updatedSession = service.getFromSession(key);
        assertNotNull(updatedSession, "Session should exist");
        assertEquals(VerifySessionStatus.INVALID, updatedSession.getStatus(), "Status should be INVALID");
    }

    @Test
    void testRejectSessionWithTicketIdThrowsExceptionWhenSessionNotFound() {
        assertThrows(InternalServerErrorException.class, () -> {
            service.rejectSession("nonExistingSession");
        });
    }
}
