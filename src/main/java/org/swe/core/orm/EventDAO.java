package org.swe.core.orm;

import org.swe.model.Event;
import java.util.ArrayList;

public interface EventDAO {
     Event getEvent(int id);
     ArrayList<Event> getAllEvents();
     boolean addEvent(Event event);
     boolean updateEvent(Event event);
     boolean deleteEvent(int id);
     
}
