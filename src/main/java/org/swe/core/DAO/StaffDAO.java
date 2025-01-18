package org.swe.core.DAO;

import java.util.List;

import org.swe.model.Event;
import org.swe.model.Staff;

public interface StaffDAO {

    boolean addStaffToEvent(int userId, int eventId);

    boolean removeStaffFromEvent(int userId, int eventId);

    List<Staff> getStaffByEventId(int eventId);

    List<Event> getEventsByStaffUserId(int userId);
}