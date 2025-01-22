package org.swe;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.swe.business.ApplicationManager;
import org.swe.business.AdminController;
import org.swe.core.DTO.CreateEventDTO;
import org.swe.core.DTO.UpdateEventDTO;
import org.swe.core.DTO.CreateUserDTO;
import org.swe.core.DBM.DBManager;
import org.swe.core.DTO.RemoveStaffFromEventDTO;  
import org.swe.core.exceptions.NotFoundException;
import org.swe.core.exceptions.UnauthorizedException;
import org.swe.model.Event;
import org.swe.core.DTO.AddStaffToEventDTO;

import java.util.Date;
import java.util.List;




@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventIT {

     static AdminController adminController;
     static String token;
     static String token2;

     @BeforeAll
     public static void setUp() {
         ApplicationManager app = new ApplicationManager();
         adminController = app.getAdminController();

         DBManager.getInstance().clearTables();
         System.out.println("Tables cleared");

         CreateUserDTO user = new CreateUserDTO("Mario", "Rossi", "simone.pellici@Stella.it", "password");
         token = adminController.signup(user);
         CreateUserDTO user2 = new CreateUserDTO("Mario", "Bianchi", "federico.donati@pipus.it", "password");
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
                    AddStaffToEventDTO staff = new AddStaffToEventDTO(e.getId(), "federico.donati@pipus.it");
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
                    AddStaffToEventDTO staff = new AddStaffToEventDTO(e.getId(), "federico.donati@pipus.it");
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
                    RemoveStaffFromEventDTO staff = new RemoveStaffFromEventDTO(e.getId(), "federico.donati@pipus.it");
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
                    RemoveStaffFromEventDTO staff = new RemoveStaffFromEventDTO(e.getId(), "federico.donati@pipus.it");
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
     @Order(12)
     public void deleteEventShouldReturnTrue() {
          List<Event> events = adminController.getAllEvents(token);
          for(Event e : events) {
               if(e.getTitle().equals("Event1")) {
                    assertTrue(adminController.deleteEvent(e.getId(), token), "Event deletion failed");
               }
          }
     }
}
