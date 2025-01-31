package org.swe;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.swe.business.ApplicationManager;
import org.swe.business.AdminController;
import org.swe.business.GuestController;
import org.swe.core.DTO.*;
import org.swe.core.DBM.DBManager;
import org.swe.core.exceptions.ApplicationException;
import org.swe.core.exceptions.NotFoundException;
import org.swe.core.exceptions.UnauthorizedException;
import org.swe.model.Event;
import org.swe.model.Staff;
import org.swe.model.Ticket;

import java.util.Date;
import java.util.List;




@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventIT {

     static AdminController adminController;
     static GuestController guestController;
     static String token;
     static String token2;

     @BeforeAll
     public static void setUp() {
         ApplicationManager app = new ApplicationManager();
         adminController = app.getAdminController();
         guestController = app.getGuestController();

         DBManager.getInstance().clearTables();
         System.out.println("Tables cleared");

         CreateUserDTO user = new CreateUserDTO("Mario", "Rossi", "simone.stella@gmail.it", "password");
         token = adminController.signup(user);
         CreateUserDTO user2 = new CreateUserDTO("Mario", "Bianchi", "federico.dona@gmail.it", "password");
         token2 = adminController.signup(user2);
     }

     @AfterAll
     public static void tearDown() {
         DBManager.getInstance().clearTables();
          }

     @Test
     @Order(1)
     public void createEventShouldReturnTrue() {
          CreateEventDTO event = new CreateEventDTO("Event1", "Description", new Date(System.currentTimeMillis() + 86400000), 100, 10);
          assertTrue(adminController.createEvent(event, token), "Event creation failed");
     }
     

     @Test
     @Order(2)
     public void getAllEventsShouldReturnList() {
          List<Event> events = adminController.getAllEvents(token);
          assertNotNull(events, "Events list is null");
     }

     @Test
     @Order(3)
     public void updateEventShouldReturnTrue() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    UpdateEventDTO event = new UpdateEventDTO(e.getId(), "Event1", "Updated_Description", new Date(System.currentTimeMillis() + 86400000), 100, 15);
                    assertTrue(adminController.updateEvent(event, token), "Event update failed");
               }
          }
     } 
     
     @Test
     @Order(4)
     public void notAdminUpdateShouldThrowException() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    UpdateEventDTO event = new UpdateEventDTO(e.getId(), "Event1", "Updated_Description", new Date(System.currentTimeMillis() + 86400000), 100, 15);
                    assertThrows(UnauthorizedException.class, () -> adminController.updateEvent(event, token2),
                            "Unauthorized exception not thrown: user who is not admin should not be able to update event");
               }
          }
     }

     @Test
     @Order(5)
     public void notAdminAddStaffShouldThrowException() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    AddStaffToEventDTO staff = new AddStaffToEventDTO(e.getId(), "federico.dona@gmail.it");
                    assertThrows(UnauthorizedException.class, () -> adminController.addStaff(staff, token2), "Unauthorized exception not thrown: user who is not admin should not be able to add staff to event");
               }
          }
     }


     @Test
     @Order(6)
     public void addStaffShouldReturnTrue() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    AddStaffToEventDTO staff = new AddStaffToEventDTO(e.getId(), "federico.dona@gmail.it");
                    assertTrue(adminController.addStaff(staff, token), "Staff addition failed");
                }
           }
     }

     @Test
     @Order(7)
     public void wrongEmailStaffShouldThrowException() {
          List<Event> events = adminController.getAllEvents(token);
          
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    AddStaffToEventDTO staff = new AddStaffToEventDTO(e.getId(), "mago@wizards.it");
                    assertThrows(NotFoundException.class, () -> adminController.addStaff(staff, token), "NotFoundException not thrown: email not found in the database");
               }
          }
     }

     @Test
     @Order(8)
     public void notAdminShouldNotRemoveStaff() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    RemoveStaffFromEventDTO staff = new RemoveStaffFromEventDTO(e.getId(), "federico.dona@gmail.it");
                    assertThrows(UnauthorizedException.class, () -> adminController.removeStaff(staff, token2), "Unauthorized exception not thrown: user who is not admin should not be able to remove staff from event");
               }
          }
     }

     @Test
     @Order(9)
     public void removeStaffShouldReturnTrue() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    RemoveStaffFromEventDTO staff = new RemoveStaffFromEventDTO(e.getId(), "federico.dona@gmail.it");
                    assertTrue(adminController.removeStaff(staff, token), "Staff removal failed");
               }
          }
     }

     @Test
     @Order(10)
     public void wrongEmailRemoveStaffShouldThrowException() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    RemoveStaffFromEventDTO staff = new RemoveStaffFromEventDTO(e.getId(), "mago@wizards.it");
                    assertThrows(NotFoundException.class, () -> adminController.removeStaff(staff, token), "NotFoundException not thrown: email not found in the database");
               }
          }
     }         

     

     @Test
     @Order(11)
     public void notAdminDeleteShouldThrowException() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    assertThrows(UnauthorizedException.class, () -> adminController.deleteEvent(e.getId(), token2), "Unauthorized exception not thrown: user who is not admin should not be able to delete event");
               }
          }
     }

     @Test
     @Order(13)
     public void eventPurchaseShouldDecrementAvailableTickets() {
          // event creation
          CreateEventDTO event = new CreateEventDTO("Event1", "Description", new Date(System.currentTimeMillis() + 86400000), 100, 10);
          assertTrue(adminController.createEvent(event, token), "Event creation failed");
          List<Event> events = adminController.getAllEvents(token);
         assertFalse(events.isEmpty(), "Event retrieval failed");
          // event purchase
          Integer eventId = events.get(0).getId();
          BuyTicketDTO buyTicketDTO = new BuyTicketDTO();
          buyTicketDTO.setEventId(eventId);
          buyTicketDTO.setQuantity(1);
          buyTicketDTO.setPaymentMethod("GOOGLE_PAY");

          Ticket ticket = guestController.buyTicket(buyTicketDTO, token);
          assertNotNull(ticket, "Ticket purchase failed");

          // check if available tickets are decremented
          Event event2 = adminController.getEventById(eventId, token);
          assertEquals(99, event2.getTicketsAvailable(), "Available tickets not decremented");

     }

     @Test
     @Order(14)
     public void eventPurchaseShouldFailIsNotEnoughTickets() {
          int ticketsAvailable = 10;
          // event creation
          CreateEventDTO event = new CreateEventDTO("Event1", "Description", new Date(System.currentTimeMillis() + 86400000), ticketsAvailable, 10);
          assertTrue(adminController.createEvent(event, token), "Event creation failed");
          List<Event> events = adminController.getAllEvents(token);
          assertFalse(events.isEmpty(), "Event retrieval failed");
          // event purchase
          BuyTicketDTO buyTicketDTO = new BuyTicketDTO();
          buyTicketDTO.setEventId(events.get(0).getId());
          buyTicketDTO.setQuantity(ticketsAvailable+1);

          assertThrows(ApplicationException.class, () -> guestController.buyTicket(buyTicketDTO, token), "NotFoundException not thrown: not enough tickets available");

     }

     @Test
     @Order(15)
     public void getEventByIdsShouldPopulateAdminsAndStaff(){
          // get all admin events
          List<Event> events = adminController.getAllEvents(token);
          assertFalse(events.isEmpty(), "Event retrieval failed");
          Integer eventId = events.get(0).getId();

          // populate staff
          AddStaffToEventDTO staff = new AddStaffToEventDTO(eventId, "federico.dona@gmail.it");
          adminController.addStaff(staff, token);

          // get event by id
          Event event = adminController.getEventById(events.get(0).getId(), token);
          assertNotNull(event, "Get event by id failed");
          // check if admins and staff are populated
          assertEquals(1, event.getAdmins().size(), "Admins not populated");
          assertFalse(event.getStaff().isEmpty(), "Staff not populated");

          // check if staff is correct
          boolean found = false;
          for (Staff s : event.getStaff()) {
               if (s.getEmail().equals("federico.dona@gmail.it")) {
                    found = true;
                    break;
               }
          }
          assertTrue(found, "Staff not populated correctly");
     }

     @Test
     @Order(16)
     public void deleteEventShouldReturnTrue() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    assertTrue(adminController.deleteEvent(e.getId(), token), "Event deletion failed");
               }
          }
     }


}
