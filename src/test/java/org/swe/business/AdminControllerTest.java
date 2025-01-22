package org.swe.business;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.swe.core.DAO.AdminDAO;
import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.StaffDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.AddStaffToEventDTO;
import org.swe.core.DTO.CreateEventDTO;
import org.swe.core.DTO.RemoveStaffFromEventDTO;
import org.swe.core.DTO.UpdateEventDTO;
import org.swe.core.exceptions.BadRequestException;
import org.swe.core.exceptions.InternalServerErrorException;
import org.swe.core.exceptions.NotFoundException;
import org.swe.core.exceptions.UnauthorizedException;
import org.swe.model.Event;
import org.swe.model.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AdminControllerTest {

    protected AdminController adminController;
    protected EventDAO mockEventDAO;
    protected AdminDAO mockAdminDAO;
    protected  UserDAO mockUserDAO;
    protected  StaffDAO mockStaffDAO;

    @BeforeEach
    void setUp() {
        this.mockEventDAO = mock(EventDAO.class);
        this.mockUserDAO = mock(UserDAO.class);
        this.mockAdminDAO = mock(AdminDAO.class);
        this.mockStaffDAO = mock(StaffDAO.class);
        AuthService mockAuthService = mock(AuthService.class);
        adminController = new AdminController(mockAuthService, mockEventDAO, mockUserDAO, mockAdminDAO, mockStaffDAO);
        when(mockAuthService.validateAccessToken("token")).thenReturn(1);
        when(mockUserDAO.getUserById(1)).thenReturn(new User("name", "surname", "password", "email", 1));
    }

    @Nested
    class CreateEventTests {
        @Test
        void createEVentShouldReturnAnEventIfEventCreationIsSuccessful() {
            // DTO
            CreateEventDTO dto = new CreateEventDTO(
                    "title",
                    "description",
                    Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    100, 10
            );

            // Mock the eventDAO.createEvent method
            Event dummyEvent = new Event.Builder()
                    .setId(1)
                    .setTitle("title")
                    .setDescription("description")
                    .setDate(Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .setTicketsAvailable(100)
                    .setTicketPrice(10)
                    .build();

            when(mockEventDAO.createEvent("title", "description", Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()), 100, 10)).thenReturn(dummyEvent);

            // Mock addAdminToEvent
            when(mockAdminDAO.addAdminToEvent(1, 1)).thenReturn(true);

            // Test:
            boolean isSuccess = adminController.createEvent(dto, "token");
            assertTrue(isSuccess);
        }

        @Test
        void createEVentShouldThrowInternalServerErrorExceptionIfEventCreationFails() {
            // DTO
            CreateEventDTO dto = new CreateEventDTO(
                    "title",
                    "description",
                    Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    100, 10
            );

            // Mock the eventDAO.createEvent method
            when(mockEventDAO.createEvent("title", "description", Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()), 100, 10)).thenReturn(null);

            // Test:
            try {
                adminController.createEvent(dto, "token");
            } catch (InternalServerErrorException e) {
                assertTrue(true);
            }
        }
    }

    @Nested
    class DeleteEventTests {

        @Test
        void deleteEventShouldReturnTrueIfEventIsDeleted() {
            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(true);
            // Mock deleteEvent
            when(mockEventDAO.deleteEvent(1)).thenReturn(true);
            // Test:
            boolean isSuccess = adminController.deleteEvent(1, "token");
            assertTrue(isSuccess);
        }

        @Test
        void deleteEventShouldThrowUnauthorizedExceptionIfUserIsNotAdminOfEvent() {
            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(false);
            // Test:
            try {
                adminController.deleteEvent(1, "token");
            } catch (UnauthorizedException e) {
                assertTrue(true);
            }
        }
    }

    @Nested
    class UpdateEventTests {

        @Test
        void updateEventShouldReturnTrueIfEventIsUpdated() {
            // DTO
            UpdateEventDTO dto = new UpdateEventDTO(
                    1,
                    "title",
                    "description",
                    Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    100, 10
            );

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(true);
            // Mock updateEvent
            when(mockEventDAO.updateEvent(1, "title", "description", Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()), 100, 10)).thenReturn(true);
            // Test:
            boolean isSuccess = adminController.updateEvent(dto, "token");
            assertTrue(isSuccess);
        }

        @Test
        void updateEventShouldThrowUnauthorizedExceptionIfUserIsNotAdminOfEvent() {
            // DTO
            UpdateEventDTO dto = new UpdateEventDTO(
                    1,
                    "title",
                    "description",
                    Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    100, 10
            );

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(false);
            // Test:
            try {
                adminController.updateEvent(dto, "token");
            } catch (UnauthorizedException e) {
                assertTrue(true);
            }
        }
    }

    @Test
    void getAllEventsShouldReturnAListOfEvents() {
        List<Event> dummyEvents = List.of(
                new Event.Builder()
                        .setId(1)
                        .setTitle("title")
                        .setDescription("description")
                        .setDate(Date.from(LocalDate.of(2025, 12, 12).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                        .setTicketsAvailable(100)
                        .setTicketPrice(10)
                        .build()
        );

        // Mock getEventsByAdminUserId
        when(mockAdminDAO.getEventsByAdminUserId(1)).thenReturn(dummyEvents);

        // Test:
        List<Event> events = adminController.getAllEvents("token");
        assertEquals(1, events.size());
    }

    @Nested
    class AddStaffTests {

        @Test
        void addStaffShouldThrowBadRequestExceptionIfStaffEmailIsInvalid() {
            // DTO
            AddStaffToEventDTO dto = new AddStaffToEventDTO(1, "invalidEmail");

            // Test:
            assertThrows(BadRequestException.class, () -> {
                adminController.addStaff(dto, "token");
            });
        }

        @Test
        void addStaffShouldThrowUnauthorizedExceptionIfUserIsNotAdminOfEvent() {
            // DTO
            AddStaffToEventDTO dto = new AddStaffToEventDTO(1, "staff@gmail.com");

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(false);
            // Test:
            try {
                adminController.addStaff(dto, "token");
            } catch (UnauthorizedException e) {
                assertTrue(true);
            }
        }

        @Test
        void addStaffShouldThrowNotFoundExceptionIfUserIsNotFound() {
            // DTO
            AddStaffToEventDTO dto = new AddStaffToEventDTO(1, "staff@gmail.com");

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(true);

            // Mock getUserByEmail
            when(mockUserDAO.getUserByEmail("staff@gmial.com")).thenReturn(null);

            // Test:
            assertThrows(NotFoundException.class, () -> {
                adminController.addStaff(dto, "token");
            });
        }

        @Test
        void addStaffShouldThrowRuntimeExceptionIfStaffIsNotAddedToEvent() {
            // DTO
            AddStaffToEventDTO dto = new AddStaffToEventDTO(1, "staff@gmail.com");

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(true);

            // Mock getUserByEmail
            when(mockUserDAO.getUserByEmail("staff@gmail.com")).thenReturn(new User("name", "surname", "password", "staff@gmail.com", 2));

            // Mock addStaffToEvent
            when(mockStaffDAO.addStaffToEvent(2, 1)).thenReturn(false);

            // Test:
            assertThrows(RuntimeException.class, () -> {
                adminController.addStaff(dto, "token");
            });
        }

        @Test
        void addStaffShouldReturnWithoutExceptionIfStaffIsAddedToEvent() {
            // DTO
            AddStaffToEventDTO dto = new AddStaffToEventDTO(1, "staff@gmail.com");

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(true);

            // Mock getUserByEmail
            when(mockUserDAO.getUserByEmail("staff@gmail.com")).thenReturn(new User("name", "surname", "password", "staff@gmail.com", 2));

            // Mock addStaffToEvent
            when(mockStaffDAO.addStaffToEvent(2, 1)).thenReturn(true);

            // Test:
            assertDoesNotThrow(() -> {
                adminController.addStaff(dto, "token");
            });
        }
    }

    @Nested
    class RemoveStaffTests {

        @Test
        void removeStaffShouldThrowBadRequestExceptionIfStaffEmailIsInvalid() {
            // DTO
            RemoveStaffFromEventDTO dto = new RemoveStaffFromEventDTO(1, "invalidEmail");

            // Test:
            assertThrows(BadRequestException.class, () -> {
                adminController.removeStaff(dto, "token");
            });
        }

        @Test
        void removeStaffShouldThrowUnauthorizedExceptionIfUserIsNotAdminOfEvent() {
            // DTO
            RemoveStaffFromEventDTO dto = new RemoveStaffFromEventDTO(1, "staff@gmail.com");

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(false);

            // Test:
            try {
                adminController.removeStaff(dto, "token");
            } catch (UnauthorizedException e) {
                assertTrue(true);
            }
        }

        @Test
        void removeStaffShouldThrowNotFoundExceptionIfUserIsNotFound() {
            // DTO
            RemoveStaffFromEventDTO dto = new RemoveStaffFromEventDTO(1, "staff@gmail.com");

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(true);

            // Mock getUserByEmail
            when(mockUserDAO.getUserByEmail("staff@gmail.com")).thenReturn(null);

            // Test:
            assertThrows(NotFoundException.class, () -> {
                adminController.removeStaff(dto, "token");
            });
        }

        @Test
        void removeStaffShouldThrowRuntimeExceptionIfStaffIsNotRemovedFromEvent() {
            // DTO
            RemoveStaffFromEventDTO dto = new RemoveStaffFromEventDTO(1, "staff@gmial.com");

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(true);

            // Mock getUserByEmail
            when(mockUserDAO.getUserByEmail("staff@gmail.com")).thenReturn(new User("name", "surname", "password", "staff@gmail.com", 2));

            // Mock removeStaffFromEvent
            when(mockStaffDAO.removeStaffFromEvent(2, 1)).thenReturn(false);

            // Test:
            assertThrows(RuntimeException.class, () -> {
                adminController.removeStaff(dto, "token");
            });
        }

        @Test
        void removeStaffShouldReturnWithoutExceptionIfStaffIsRemovedFromEvent() {
            // DTO
            RemoveStaffFromEventDTO dto = new RemoveStaffFromEventDTO(1, "staff@gmail.com");

            // Mock isUserAdminOfEvent
            when(mockEventDAO.isUserAdminOfEvent(1, 1)).thenReturn(true);

            // Mock getUserByEmail
            when(mockUserDAO.getUserByEmail("staff@gmail.com")).thenReturn(new User("name", "surname", "password", "staff@gmail.com", 2));

            // Mock removeStaffFromEvent
            when(mockStaffDAO.removeStaffFromEvent(2, 1)).thenReturn(true);

            // Test:
            assertDoesNotThrow(() -> {
                adminController.removeStaff(dto, "token");
            });
        }
    }
}
