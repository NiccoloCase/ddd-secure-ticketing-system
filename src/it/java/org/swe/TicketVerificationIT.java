package org.swe;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.swe.core.exceptions.BadRequestException;
import org.swe.model.PaymentMethod;
import org.swe.model.StartVerificationSessionRes;
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
    private static StartVerificationSessionRes startVerificationSessionRes;

    @BeforeAll
    public static void setUp() {

        ApplicationManager app = new ApplicationManager();
        adminController = app.getAdminController();
        staffController = app.getStaffController();
        guestController = app.getGuestController();

        DBManager.getInstance().clearTables();
    }

    @AfterAll
    public static void tearDown() {
        DBManager.getInstance().clearTables();
    }

    @Test
    @Order(1)
    public void createAdminAndAddStaff() {
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
                "New Years Eve Party",
                "Description",
                Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                100,
                50);

        boolean created = adminController.createEvent(createEventDTO, adminToken);
        assertTrue(created, "The event should be created successfully");

        var allEvents = adminController.getAllEvents(adminToken);
        assertFalse(allEvents.isEmpty(), "The list of events for the admin should not be empty");

        eventId = allEvents.get(allEvents.size() - 1).getId();
        assertNotNull(eventId, "The eventId should not be null");
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

        Ticket purchased = guestController.buyTicket(buyTicketDTO, guestToken);
        assertNotNull(purchased, "The purchased ticket should not be null");
        assertEquals(2, purchased.getQuantity(), "The quantity of the purchased ticket should be equal to the quantity bought");
        assertEquals(eventId, purchased.getEventId(), "The eventId of the purchased ticket should be the same as the event created");
    }

    @Test
    @Order(4)
    public void staffStartsVerificationSession() {
        StartVerificationSessionDTO startDTO = new StartVerificationSessionDTO(eventId);

        startVerificationSessionRes = staffController.startVerificationSession(startDTO, staffToken);

        assertNotNull(startVerificationSessionRes.getVerificationCode(), "Session verification code should not be null");
        assertNotNull(startVerificationSessionRes.getKey(), "Verification session key should not be null");
    }

    @Test
    @Order(5)
    public void guestScansStaffCodeSuccessfully() {
        String verificationCode = startVerificationSessionRes.getVerificationCode();
        ScanStaffVerificationCodeDTO scanDTO = new ScanStaffVerificationCodeDTO();
        scanDTO.setCode(verificationCode);

        boolean result = guestController.scanStaffVerificationCode(scanDTO, guestToken);
        assertTrue(result, "Scan should be successful");
    }

    @Test
    @Order(6)
    public void staffValidatesSession() {
        String sessionKey = startVerificationSessionRes.getKey();

        GetVerificationSessionResultDTO dto = new GetVerificationSessionResultDTO(sessionKey);
        VerificationSessionResult result = staffController.validateVerificationSession(dto, staffToken);

        assertNotNull(result, "Result should not be null");
        assertEquals(VerifySessionStatus.VALIDATED, result.getStatus(),
                "After validation the status should be VALIDATED");

        assertEquals(2, result.getValidatedTickets(),
                "The number of validated tickets should be equal to the number of tickets bought by the guest");
    }

    @Test
    @Order(7)
    public void staffCannotValidateSessionBelongingToAnotherStaff() {

        CreateUserDTO otherStaffDto = new CreateUserDTO("Another", "Staff", "anotherstaff@test.it", "password");
        String otherStaffToken = staffController.signup(otherStaffDto);
        assertNotNull(otherStaffToken);

        StartVerificationSessionDTO startDTO = new StartVerificationSessionDTO(eventId);
        StartVerificationSessionRes otherStaffSession = staffController.startVerificationSession(startDTO, otherStaffToken);
        String otherStaffSessionKey = otherStaffSession.getKey();

        // Trying to validate the session created by staff with another staff token
        GetVerificationSessionResultDTO dto = new GetVerificationSessionResultDTO(otherStaffSessionKey);

        assertThrows(BadRequestException.class, () -> {
            staffController.validateVerificationSession(dto, staffToken);
        });
    }

    @Test
    @Order(8)
    public void staffCannotValidateAlreadyValidatedSession() {
        //  Create a new staff and guest
        CreateUserDTO tempStaffDto = new CreateUserDTO("TempStaff", "TempSurname", "tempstaff@test.it", "password");
        String tempStaffToken = staffController.signup(tempStaffDto);
        assertNotNull(tempStaffToken);

        CreateUserDTO tempGuestDto = new CreateUserDTO("TempGuest", "TempSurname", "tempguest@test.it", "password");
        String tempGuestToken = guestController.signup(tempGuestDto);
        assertNotNull(tempGuestToken);

        BuyTicketDTO buyTicketDTO = new BuyTicketDTO();
        buyTicketDTO.setEventId(eventId);
        buyTicketDTO.setQuantity(3);
        buyTicketDTO.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        guestController.buyTicket(buyTicketDTO, tempGuestToken);

        // Start the verification session
        StartVerificationSessionDTO startDTO = new StartVerificationSessionDTO(eventId);
        StartVerificationSessionRes tempSession = staffController.startVerificationSession(startDTO, tempStaffToken);
        String sessionKey = tempSession.getKey();
        assertNotNull(sessionKey);

        ScanStaffVerificationCodeDTO scanDTO = new ScanStaffVerificationCodeDTO();
        scanDTO.setCode(tempSession.getVerificationCode()); 
        guestController.scanStaffVerificationCode(scanDTO, tempGuestToken);

        // Staff validation ( I attempt )
        GetVerificationSessionResultDTO dto = new GetVerificationSessionResultDTO(sessionKey);
        VerificationSessionResult result = staffController.validateVerificationSession(dto, tempStaffToken);
        assertNotNull(result);

        assertEquals(VerifySessionStatus.VALIDATED, result.getStatus());

        // Trying to validate again
        assertThrows(BadRequestException.class, () -> {
            staffController.validateVerificationSession(dto, tempStaffToken);
        }, "Session already validated");
    }

    @Test
    @Order(9)
    public void staffCannotValidateSessionIfUserHasNoTicketsForThatEvent() {
        // new Staff
        CreateUserDTO staffDto = new CreateUserDTO("NoTicketStaff", "TempSurname", "noticketstaff@test.it", "password");
        String noTicketStaffToken = staffController.signup(staffDto);

        // Guest WITHOUT a ticket for the event
        CreateUserDTO guestDto = new CreateUserDTO("NoTicketGuest", "TempSurname", "noticketguest@test.it", "password");
        String noTicketGuestToken = guestController.signup(guestDto);

        // Start the verification session
        StartVerificationSessionDTO startDTO = new StartVerificationSessionDTO(eventId);
        StartVerificationSessionRes sr = staffController.startVerificationSession(startDTO, noTicketStaffToken);

        // Guest scan
        ScanStaffVerificationCodeDTO scanDTO = new ScanStaffVerificationCodeDTO();
        scanDTO.setCode(sr.getVerificationCode());
        guestController.scanStaffVerificationCode(scanDTO, noTicketGuestToken);


        // Failure
        GetVerificationSessionResultDTO dto = new GetVerificationSessionResultDTO(sr.getKey());
        assertThrows(BadRequestException.class, () -> {
            staffController.validateVerificationSession(dto, noTicketStaffToken);
        }, "Guest has no tickets for the event: validation should fail");
    }
}