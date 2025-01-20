package org.swe.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.swe.core.exceptions.InternalServerErrorException;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

import static org.junit.jupiter.api.Assertions.*;

class ConcreteVerifySessionServiceTest {

    private ConcreteVerifySessionService service;

    @BeforeEach
    void setUp() {
        service = new ConcreteVerifySessionService();
    }

    @Test
    void testAddToSession() {
        VerifySession session = new VerifySession(1, 1001);
        String key = service.addToSession(session);

        VerifySession retrievedSession = service.getFromSession(key);
        assertNotNull(retrievedSession, "Session should be retrieved successfully");
        assertEquals(session, retrievedSession, "The added session and retrieved session should be the same");
    }

    @Test
    void testUniqueSessionKeys() {
        VerifySession session1 = new VerifySession(1, 1001);
        VerifySession session2 = new VerifySession(2, 1002);
        String key1 = service.addToSession(session1);
        String key2 = service.addToSession(session2);

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
        String key = service.addToSession(session);
        service.removeFromSession(key);

        VerifySession retrievedSession = service.getFromSession(key);
        assertNull(retrievedSession, "The session should be removed and not retrievable");
    }

    @Test
    void testIsInSession() {
        VerifySession session = new VerifySession(1, 1001);
        String key = service.addToSession(session);

        assertTrue(service.isInSession(key), "The session should exist in the session data");
        assertFalse(service.isInSession("nonExistingSession"), "The session should not exist in the session data");
    }

    @Test
    void testClearSession() {
        VerifySession session1 = new VerifySession(1, 1001);
        VerifySession session2 = new VerifySession(2, 1002);
        String key1 = service.addToSession(session1);
        String key2 = service.addToSession(session2);

        service.clearSession();

        assertFalse(service.isInSession(key1), "Session 1 should be cleared");
        assertFalse(service.isInSession(key2), "Session 2 should be cleared");
    }

    @Test
    void testValidateSession() {
        VerifySession session = new VerifySession(1, 1001);
        String key = service.addToSession( session);

        service.validateSession(key);

        VerifySession updatedSession = service.getFromSession(key);
        assertNotNull(updatedSession, "Session should exist");
        //assertEquals(999, updatedSession.getTicketId(), "Ticket ID should be updated");
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
        String key = service.addToSession( session);

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
        String key = service.addToSession(session);

        service.rejectSession(key);

        VerifySession updatedSession = service.getFromSession(key);
        assertNotNull(updatedSession, "Session should exist");
        //assertEquals(999, updatedSession.getTicketId(), "Ticket ID should be set");
        assertEquals(VerifySessionStatus.INVALID, updatedSession.getStatus(), "Status should be INVALID");
    }

    @Test
    void testRejectSessionWithTicketIdThrowsExceptionWhenSessionNotFound() {
        assertThrows(InternalServerErrorException.class, () -> {
            service.rejectSession("nonExistingSession");
        });
    }

}
