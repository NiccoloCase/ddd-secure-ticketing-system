package org.swe;

import org.swe.core.Config;
import org.swe.core.DAO.*;
import org.swe.core.dbManager.DBManager;
import org.swe.model.*;



public class Main {
    public static void main(String[] args) {
        // Load the configurations
        Config.init();

        DBManager dbManager = DBManager.getInstance();

        // DAO test
        System.out.println("DAO test");
        TicketDAO ticketDAO = new ConcreteTicketDAO();
        //UserDAO userDAO = new ConcreteUserDAO();
        EventDAO eventDAO = new ConcreteEventDAO();

        // for each DAO add 3 elements find one by id then find all, update one of them and list all again
        // then delete everithing
        /*System.out.println("TicketDAO test");
        for (int i = 0; i < 3; i++) {
            ticketDAO.deleteTicket(i+1);
        }

        for (int i = 0; i < 3; i++) {
            Ticket ticket = new Ticket(i+1, i+1, i+1, false);
            ticketDAO.addTicket(ticket);
        }
        System.out.println(ticketDAO.getTicketById(1));
        System.out.println(ticketDAO.getAllTickets());
        Ticket ticket = ticketDAO.getTicketById(1); 
        ticket.setUsed(true);
        ticketDAO.updateTicket(ticket);
        System.out.println(ticketDAO.getAllTickets());
        for (int i = 0; i < 3; i++) {
            ticketDAO.deleteTicket(i+1);
        }*/

        System.out.println("EventDAO test");

        for (int i = 0; i < 3; i++) {
            Event event = new Event.Builder()
                .setId(i+1)
                .setTitle("title" + i)
                .setDescription("description" + i)
                .setDate(new java.util.Date())
                .setDate(new java.util.Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000))
                .setTicketsAvailable(i)
                .setTicketPrice(i)
                .build();
            eventDAO.addEvent(event);
        }

        eventDAO.getAllEvents().forEach(System.out::println);
        Event event = eventDAO.getEvent(1);
        System.out.println(event);
        //update event title and description and ticketsAvailable, keep the same date and price
        //event class has a builder pattern
        event = new Event.Builder()
            .setId(event.getId())
            .setTitle("new title")
            .setDescription("new description")
            .setDate(event.getDate())
            .setTicketsAvailable(100)
            .setTicketPrice(event.getTicketPrice())
            .build();
        eventDAO.updateEvent(event);
        //eventDAO.getAllEvents().forEach(System.out::println);
        for (int i = 0; i < 3; i++) {
            eventDAO.deleteEvent(i+1);
        }

        DBManager.close();
    }
}