package org.swe.core.DAO;

import java.util.ArrayList;
import java.util.Date;

import org.swe.model.Event;

public interface EventDAO {
    Event getEventById(int id);
    ArrayList<Event> getAllEvents();
    Event createEvent(String title, String description, Date date, int ticketsAvailable, double ticketPrice);
    boolean updateEvent(int id, String title, String description, Date date, int ticketsAvailable, double ticketPrice);
    boolean deleteEvent(int id);
}