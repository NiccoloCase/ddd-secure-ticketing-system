package org.swe.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.swe.core.exceptions.InternalServerErrorException;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

import static org.junit.jupiter.api.Assertions.*;

class VerifySessionServiceImplTest {

    private VerifySessionServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VerifySessionServiceImpl();
    }

    @Test
    void testAddToSession() {
        VerifySession session = new VerifySession(1, 1001, 123);
        service.addToSession("session1", session);

        VerifySession retrievedSession = service.getFromSession("session1");
        assertNotNull(retrievedSession, "Session should be retrieved successfully");
        assertEquals(session, retrievedSession, "The added session and retrieved session should be the same");
    }

    @Test
    void testGetFromSessionWhenNotExists() {
        VerifySession retrievedSession = service.getFromSession("nonExistingSession");
        assertNull(retrievedSession, "The session should return null if it doesn't exist");
    }

    @Test
    void testRemoveFromSession() {
        VerifySession session = new VerifySession(1, 1001, 123);
        service.addToSession("session1", session);
        service.removeFromSession("session1");

        VerifySession retrievedSession = service.getFromSession("session1");
        assertNull(retrievedSession, "The session should be removed and not retrievable");
    }

    @Test
    void testIsInSession() {
        VerifySession session = new VerifySession(1, 1001, 123);
        service.addToSession("session1", session);

        assertTrue(service.isInSession("session1"), "The session should exist in the session data");
        assertFalse(service.isInSession("nonExistingSession"), "The session should not exist in the session data");
    }

    @Test
    void testClearSession() {
        VerifySession session1 = new VerifySession(1, 1001, 123);
        VerifySession session2 = new VerifySession(2, 1002, 124);
        service.addToSession("session1", session1);
        service.addToSession("session2", session2);

        service.clearSession();

        assertFalse(service.isInSession("session1"), "Session 1 should be cleared");
        assertFalse(service.isInSession("session2"), "Session 2 should be cleared");
    }

    @Test
    void testVerifySession() {
        VerifySession session = new VerifySession(1, 1001, 123);
        service.addToSession("session1", session);

        service.verifySession("session1", 999);

        VerifySession updatedSession = service.getFromSession("session1");
        assertNotNull(updatedSession, "Session should exist");
        assertEquals(999, updatedSession.getTicketId(), "Ticket ID should be updated");
        assertEquals(VerifySessionStatus.VERIFIED, updatedSession.getStatus(), "Status should be VERIFIED");
    }

    @Test
    void testVerifySessionThrowsExceptionWhenSessionNotFound() {
        InternalServerErrorException exception = assertThrows(InternalServerErrorException.class, () -> {
            service.verifySession("nonExistingSession", 999);
        });
    }

    @Test
    void testRejectSession() {
        VerifySession session = new VerifySession(1, 1001, 123);
        service.addToSession("session1", session);

        service.rejectSession("session1");

        VerifySession updatedSession = service.getFromSession("session1");
        assertNotNull(updatedSession, "Session should exist");
        assertEquals(VerifySessionStatus.INVALID, updatedSession.getStatus(), "Status should be INVALID");
    }

    @Test
    void testRejectSessionThrowsExceptionWhenSessionNotFound() {
        InternalServerErrorException exception = assertThrows(InternalServerErrorException.class, () -> {
            service.rejectSession("nonExistingSession");
        });
    }

    @Test
    void testRejectSessionWithTicketId() {
        VerifySession session = new VerifySession(1, 1001, 123);
        service.addToSession("session1", session);

        service.rejectSession("session1", 999);

        VerifySession updatedSession = service.getFromSession("session1");
        assertNotNull(updatedSession, "Session should exist");
        assertEquals(999, updatedSession.getTicketId(), "Ticket ID should be set");
        assertEquals(VerifySessionStatus.INVALID, updatedSession.getStatus(), "Status should be INVALID");
    }

    @Test
    void testRejectSessionWithTicketIdThrowsExceptionWhenSessionNotFound() {
        InternalServerErrorException exception = assertThrows(InternalServerErrorException.class, () -> {
            service.rejectSession("nonExistingSession", 999);
        });
    }

}
