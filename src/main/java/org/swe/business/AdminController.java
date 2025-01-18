package org.swe.business;

import java.util.List;

import org.swe.core.DAO.AdminDAO;
import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.StaffDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.DTO.AddStaffToEventDTO;
import org.swe.core.DTO.CreateEventDTO;
import org.swe.core.DTO.RemoveStaffFromEventDTO;
import org.swe.core.DTO.UpdateEventDTO;
import org.swe.core.exceptions.InternalServerErrorException;
import org.swe.core.exceptions.NotFoundException;
import org.swe.core.exceptions.UnauthorizedException;
import org.swe.model.Event;
import org.swe.model.User;

public class AdminController extends UserController {

    private EventDAO eventDAO;
    private UserDAO userDAO;
    private AdminDAO adminDAO;
    private StaffDAO staffDAO;

    public AdminController(AuthService authService, EventDAO eventDAO, UserDAO userDAO, AdminDAO adminDAO,
            StaffDAO staffDAO) {
        super(authService,userDAO);
        this.eventDAO = eventDAO;
        this.userDAO = userDAO;
        this.adminDAO = adminDAO;
        this.staffDAO = staffDAO;
    }

    public boolean createEvent(CreateEventDTO dto, String token) throws InternalServerErrorException {
        validationInterceptor(dto);
        User user = authInterceptor(token);

        Event newEvent = eventDAO.createEvent(
                dto.getTitle(),
                dto.getDescription(),
                dto.getDate(),
                dto.getTicketsAvailable(),
                dto.getTicketPrice());
        if (newEvent == null) {
            throw new InternalServerErrorException("Event creation failed");
        }
        boolean success = adminDAO.addAdminToEvent(user.getId(), newEvent.getId());
        if (!success) {
            throw new InternalServerErrorException("Failed to add user as admin to event");
        }

        return true;
    }

    public boolean deleteEvent(int eventId, String token) throws UnauthorizedException {
        validationInterceptor(eventId);
        User user = authInterceptor(token);

        boolean isAdmin = eventDAO.isUserAdminOfEvent(user.getId(), eventId);
        if (!isAdmin) {
            throw new UnauthorizedException("Not authorized: user is not Admin of this event.");
        }

        return eventDAO.deleteEvent(eventId);
    }

    public boolean updateEvent(UpdateEventDTO dto, String token) throws UnauthorizedException {
        validationInterceptor(dto);
        User user = authInterceptor(token);

        boolean isAdmin = eventDAO.isUserAdminOfEvent(user.getId(), dto.getEventId());
        if (!isAdmin) {
            throw new UnauthorizedException("Not authorized: user is not Admin of this event.");
        }

        return eventDAO.updateEvent(
                dto.getEventId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getDate(),
                dto.getTicketsAvailable(),
                dto.getTicketPrice());
    }

    public List<Event> getAllEvents(String token) {
        User user = authInterceptor(token);

        // restituisce solo eventi di cui è admin.

        return adminDAO.getEventsByAdminUserId(user.getId());
    }

    public void addStaff(AddStaffToEventDTO dto, String token) {
        validationInterceptor(dto);
        User user = authInterceptor(token);

        boolean isAdmin = eventDAO.isUserAdminOfEvent(user.getId(), dto.getEventId());
        if (!isAdmin) {
            throw new UnauthorizedException("Not authorized: user is not Admin of this event.");
        }

        User dbUser = userDAO.getUserByEmail(dto.getStaffEmail());
        if (dbUser == null) {
            throw new NotFoundException("User not found.");
        }

        boolean success = staffDAO.addStaffToEvent(dbUser.getId(), dto.getEventId());
        if (!success) {
            throw new RuntimeException("Failed to add staff to event.");
        }
    }

    public void removeStaff(RemoveStaffFromEventDTO dto, String token) {
        validationInterceptor(dto);
        User user = authInterceptor(token);

        boolean isAdmin = eventDAO.isUserAdminOfEvent(user.getId(), dto.getEventId());
        if (!isAdmin) {
            throw new UnauthorizedException("Not authorized: user is not Admin of this event.");
        }

        User dbUser = userDAO.getUserByEmail(dto.getStaffEmail());
        if (dbUser == null) {
            throw new NotFoundException("User not found.");
        }

        boolean success = staffDAO.removeStaffFromEvent(dbUser.getId(), dto.getEventId());
        if (!success) {
            throw new RuntimeException("Failed to remove staff from event.");
        }
    }

}