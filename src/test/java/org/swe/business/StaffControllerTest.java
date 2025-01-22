package org.swe.business;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.swe.core.DAO.TicketDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.ValidateVerificationSessionDTO;
import org.swe.core.DTO.StartVerificationSessionDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.core.exceptions.UnauthorizedException;
import org.swe.model.StartVerificationSessionResult;
import org.swe.model.Ticket;
import org.swe.model.User;
import org.swe.model.VerifySession;
import org.swe.model.VerifySessionStatus;

class StaffControllerTest {

    private StaffController staffController;
    private AuthService mockAuthService;
    private VerifySessionService mockVerifySessionService;
    private TicketDAO mockTicketDAO;

    @BeforeEach
    void setUp() {
        mockAuthService = mock(AuthService.class);
        mockVerifySessionService = mock(VerifySessionService.class);
        mockTicketDAO = mock(TicketDAO.class);
        UserDAO mockUserDAO = mock(UserDAO.class);

        staffController = new StaffController(
                mockAuthService,
                mockVerifySessionService,
                mockUserDAO,
                mockTicketDAO);

        when(mockAuthService.validateAccessToken("validToken")).thenReturn(10);
        when(mockUserDAO.getUserById(10))
                .thenReturn(new User("StaffName", "StaffSurname", "password", "staff@test.it", 10));
    }

    @Nested
    class StartVerificationSessionTests {

        @Test
        void startVerificationSessionShouldReturnSessionResponseIfValid() {
            StartVerificationSessionDTO dto = new StartVerificationSessionDTO(100);
            VerifySession verifySession = new VerifySession(10, 100);
            StartVerificationSessionResult fakeResponse = new StartVerificationSessionResult("someKey", "someVerificationCode");
            when(mockVerifySessionService.addToSession(any(VerifySession.class)))
                    .thenReturn(fakeResponse);

            StartVerificationSessionResult response = staffController.startVerificationSession(dto, "validToken");

            assertNotNull(response);
            assertEquals("someKey", response.getKey());
            assertEquals("someVerificationCode", response.getVerificationCode());
            verify(mockVerifySessionService, times(1)).addToSession(any(VerifySession.class));
        }

        @Test
        void startVerificationSessionShouldThrowUnauthorizedExceptionIfTokenIsInvalid() {
            when(mockAuthService.validateAccessToken("invalidToken"))
                    .thenThrow(new UnauthorizedException("Invalid token"));

            StartVerificationSessionDTO dto = new StartVerificationSessionDTO(1);

            assertThrows(UnauthorizedException.class, () -> {
                staffController.startVerificationSession(dto, "invalidToken");
            });
        }
    }

    @Nested
    class ValidateVerificationSessionTests {

        @Test
        void validateVerificationSessionShouldThrowIfSessionDoesNotExist() {
            ValidateVerificationSessionDTO dto = new ValidateVerificationSessionDTO("fakeSessionKey");
            when(mockVerifySessionService.getFromSession("fakeSessionKey"))
                    .thenThrow(new RuntimeException("DB access or session not found"));

            assertThrows(BadRequestException.class, () -> {
                staffController.validateVerificationSession(dto, "validToken");
            });
        }

        @Test
        void validateVerificationSessionShouldThrowIfSessionBelongsToAnotherStaff() {
            // create fake session belonging to another staff
            ValidateVerificationSessionDTO dto = new ValidateVerificationSessionDTO("mySessionKey");
            VerifySession vs = new VerifySession(99, 100);
            vs.setStatus(VerifySessionStatus.PENDING);
            vs.linkSessionToUser(new User("Guest", "Surname", "pwd", "guest@test.it", 20));

            when(mockVerifySessionService.getFromSession("mySessionKey")).thenReturn(vs);

            BadRequestException ex = assertThrows(BadRequestException.class, () -> {
                staffController.validateVerificationSession(dto, "validToken");
            });
            assertEquals("Session does not belong to the current staff member", ex.getMessage());
        }

        @Test
        void validateVerificationSessionShouldThrowIfSessionIsAlreadyValidated() {
            ValidateVerificationSessionDTO dto = new ValidateVerificationSessionDTO("mySessionKey");
            VerifySession vs = new VerifySession(10, 100);

            vs.linkSessionToUser(new User("Guest", "Surname", "pwd", "guest@test.it", 20));
            vs.setStatus(VerifySessionStatus.VALIDATED);
            when(mockVerifySessionService.getFromSession("mySessionKey")).thenReturn(vs);

            BadRequestException ex = assertThrows(BadRequestException.class, () -> {
                staffController.validateVerificationSession(dto, "validToken");
            });
            assertEquals("Session is already validated", ex.getMessage());
        }

        @Test
        void validateVerificationSessionShouldThrowIfSessionIsPendingOrNoGuest() {
            ValidateVerificationSessionDTO dto = new ValidateVerificationSessionDTO("mySessionKey");
            VerifySession vs = new VerifySession(10, 100);
            vs.setStatus(VerifySessionStatus.PENDING);
            vs.linkSessionToUser(null);

            when(mockVerifySessionService.getFromSession("mySessionKey")).thenReturn(vs);

            BadRequestException ex = assertThrows(BadRequestException.class, () -> {
                staffController.validateVerificationSession(dto, "validToken");
            });
            assertEquals("Session is still pending or there is no guest linked to the session", ex.getMessage());
        }

        @Test
        void validateVerificationSessionShouldThrowIfNoTicketsFound() {
            ValidateVerificationSessionDTO dto = new ValidateVerificationSessionDTO("mySessionKey");
            VerifySession vs = new VerifySession(10, 100);
            vs.setStatus(VerifySessionStatus.PENDING);
            User guestUser = new User("Guest", "Surname", "pwd", "guest@test.it", 20);
            vs.linkSessionToUser(guestUser);

            when(mockVerifySessionService.getFromSession("mySessionKey")).thenReturn(vs);

            ArrayList<Ticket> tickets = new ArrayList<>(); // <------ EMPTY ARRAY OF TICKETS!
            when(mockTicketDAO.getTicketsByUserAndEvent(20, 100))
                    .thenReturn(tickets);

            BadRequestException ex = assertThrows(BadRequestException.class, () -> {
                staffController.validateVerificationSession(dto, "validToken");
            });
            assertEquals("No tickets found for the user and event", ex.getMessage());
        }

        @Test
        void validateVerificationSessionShouldThrowIfAllTicketsUsed() {
            ValidateVerificationSessionDTO dto = new ValidateVerificationSessionDTO("mySessionKey");
            VerifySession vs = new VerifySession(10, 100);
            vs.setStatus(VerifySessionStatus.PENDING);
            User guestUser = new User("Guest", "Surname", "pwd", "guest@test.it", 20);
            vs.linkSessionToUser(guestUser);

            when(mockVerifySessionService.getFromSession("mySessionKey")).thenReturn(vs);

            Ticket usedTicket = new Ticket(1, 20, 100, 2, true);
            when(mockTicketDAO.getTicketsByUserAndEvent(20, 100)).thenReturn(new ArrayList<Ticket>() {
                {
                    add(usedTicket); // <-- THE ONLY TICKET THE USER HAS IS ALREADY USED!
                }
            });

            BadRequestException ex = assertThrows(BadRequestException.class, () -> {
                staffController.validateVerificationSession(dto, "validToken");
            });
            assertEquals("No valid tickets found for the user and event", ex.getMessage());
        }
    }
}
