package org.swe;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.swe.business.AdminController;
import org.swe.business.ApplicationManager;
import org.swe.business.GuestController;
import org.swe.business.StaffController;
import org.swe.core.DBM.DBManager;
import org.swe.core.DTO.BuyTicketDTO;
import org.swe.core.DTO.CreateEventDTO;
import org.swe.core.DTO.CreateUserDTO;
import org.swe.core.DTO.GetVerificationSessionResultDTO;
import org.swe.core.DTO.ScanStaffVerificationCodeDTO;
import org.swe.core.DTO.StartVerificationSessionDTO;
import org.swe.model.PaymentMethod;
import org.swe.model.SessionResponse;
import org.swe.model.Ticket;
import org.swe.model.VerificationSessionResult;
import org.swe.model.VerifySessionStatus;

@TestMethodOrder(OrderAnnotation.class)
public class TicketVerificationIT {

    private static AdminController adminController;
    private static StaffController staffController;
    private static GuestController guestController;

    private static String adminToken;
    private static String staffToken;
    private static String guestToken;

    private static Integer eventId;
    private static SessionResponse sessionResponse;

    @BeforeAll
    public static void setUp() {

        ApplicationManager app = new ApplicationManager();
        adminController = app.getAdminController();
        staffController = app.getStaffController();
        guestController = app.getGuestController();

        DBManager.getInstance().clearTables();
        System.out.println("Tables cleared");
    }

    @AfterAll
    public static void tearDown() {
        DBManager.getInstance().clearTables();
        System.out.println("Tables cleared (tearDown)");
    }

    @Test
    @Order(1)
    public void createAdminAndStaff() {

        CreateUserDTO adminDto = new CreateUserDTO("AdminName", "AdminSurname", "admin@test.it", "password");
        adminToken = adminController.signup(adminDto);
        assertNotNull(adminToken, "adminToken should not be null");

        CreateUserDTO staffDto = new CreateUserDTO("StaffName", "StaffSurname", "staff@test.it", "password");
        staffToken = staffController.signup(staffDto);
        System.out.println("staffToken: " + staffToken);
        assertNotNull(staffToken, "staffToken should not be null");
    }

    @Test
    @Order(2)
    public void createEvent() {

        CreateEventDTO createEventDTO = new CreateEventDTO(
                "Concerto di Capodanno",
                "Descrizione dell'evento",
                Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                100,
                50);

        boolean created = adminController.createEvent(createEventDTO, adminToken);
        assertTrue(created, "L'evento dovrebbe essere creato correttamente.");

        var allEvents = adminController.getAllEvents(adminToken);
        assertFalse(allEvents.isEmpty(), "Dovremmo trovare almeno un evento creato dall'admin");

        eventId = allEvents.get(allEvents.size() - 1).getId();
        assertNotNull(eventId, "eventId should not be null");
    }

    @Test
    @Order(3)
    public void createGuestAndBuyTicket() {

        CreateUserDTO guestDto = new CreateUserDTO("Mario", "Rossi", "guest@test.it", "password");
        guestToken = guestController.signup(guestDto);
        assertNotNull(guestToken);

        BuyTicketDTO buyTicketDTO = new BuyTicketDTO();
        buyTicketDTO.setEventId(eventId);
        buyTicketDTO.setQuantity(2);
        buyTicketDTO.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        System.out.println("guestToken: " + guestToken);
        System.out.println("eventId: " + eventId);

        Ticket purchased = guestController.buyTicket(buyTicketDTO, guestToken);
        assertNotNull(purchased, "Il ticket appena comprato non deve essere null");
        assertEquals(2, purchased.getQuantity(), "Dovrebbe aver acquistato 2 biglietti");
        assertEquals(eventId, purchased.getEventId(), "L'evento del ticket dovrebbe corrispondere a quello creato");
    }

    @Test
    @Order(4)
    public void staffStartsVerificationSession() {
        StartVerificationSessionDTO startDTO = new StartVerificationSessionDTO(eventId);

        sessionResponse = staffController.startVerificationSession(startDTO, staffToken);
        String verificationCode = sessionResponse.getVerificationCode();
        assertNotNull(verificationCode, "La sessionKey non deve essere nulla");
    }

    @Test
    @Order(5)
    public void guestScansStaffCode() {

        String verificationCode = sessionResponse.getVerificationCode();
        ScanStaffVerificationCodeDTO scanDTO = new ScanStaffVerificationCodeDTO();
        scanDTO.setCode(verificationCode);

        boolean result = guestController.scanStaffVerificationCode(scanDTO, guestToken);
        assertTrue(result, "La scansione dovrebbe andare a buon fine e restituire true");
    }

    @Test
    @Order(6)
    public void staffValidatesSession() {

        String sessionKey = sessionResponse.getKey();

        GetVerificationSessionResultDTO dto = new GetVerificationSessionResultDTO(sessionKey);

        VerificationSessionResult result = staffController.validateVerificationSession(dto, staffToken);

        assertNotNull(result, "Il result non deve essere nullo");
        assertEquals(VerifySessionStatus.VALIDATED, result.getStatus(),
                "Lo stato dovrebbe essere VALIDATED dopo un acquisto valido");

        assertEquals(2, result.getValidatedTickets(),
                "Dovrebbero essere validati 2 ticket (quanti ne ha comprati il guest)");
    }
}