package org.swe.core.DAO;

import java.util.List;

import org.swe.model.Admin;
import org.swe.model.Event;

public interface AdminDAO {

    boolean addAdminToEvent(int userId, int eventId);

    boolean removeAdminFromEvent(int userId, int eventId);

    List<Admin> getAdminsByEventId(int eventId);

    List<Event> getEventsByAdminUserId(int userId);
}