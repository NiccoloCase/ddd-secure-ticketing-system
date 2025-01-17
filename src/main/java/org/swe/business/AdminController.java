package org.swe.business;

import java.util.List;

import org.swe.core.DAO.EventDAO;
import org.swe.core.DAO.UserDAO;
import org.swe.core.dto.AddStaffToEventDTO;
import org.swe.core.dto.CreateEventDTO;
import org.swe.core.dto.RemoveStaffFromEventDTO;
import org.swe.core.dto.UpdateEventDTO;
import org.swe.core.exceptions.NotFoundException;
import org.swe.core.exceptions.UnauthorizedException;
import org.swe.model.Admin;
import org.swe.model.Event;
import org.swe.model.User;

public class AdminController extends UserController {

    private EventDAO eventDAO;
    private UserDAO userDAO;

    public AdminController(AuthService authService) {
        super(authService);
    }

    public boolean createEvent(CreateEventDTO dto, String token) {

        User user = validateInterceptor(token);

        // TODO: Forse levare? guest e staff possono creare eventi?
        if (!(user instanceof Admin)) {
            throw new UnauthorizedException("Not authorized. Must be admin.");
        }

        Event event = new Event.Builder()
                .setTitle(dto.getTitle())
                .setDescription(dto.getDescription())
                .setDate(dto.getDate())
                .setTicketsAvailable(dto.getTicketsAvailable())
                .setTicketPrice(dto.getTicketPrice())
                .build();

        return eventDAO.addEvent(event);
    }

    public boolean deleteEvent(int eventId, String token) {
        User user = validateInterceptor(token);
        if (!(user instanceof Admin)) {
            throw new UnauthorizedException("Not authorized. Must be admin.");
        }
        return eventDAO.deleteEvent(eventId);
    }

    public boolean updateEvent(UpdateEventDTO dto, String token) {
        User user = validateInterceptor(token);
        if (!(user instanceof Admin)) {
            throw new UnauthorizedException("Not authorized. Must be admin.");
        }

        Event event = new Event.Builder()
                .setTitle(dto.getTitle())
                .setDescription(dto.getDescription())
                .setDate(dto.getDate())
                .setTicketsAvailable(dto.getTicketsAvailable())
                .setTicketPrice(dto.getTicketPrice())
                .setId(dto.getEventId())
                .build();

        return eventDAO.updateEvent(event);
    }

    public List<Event> getAllEvents(String token) {
        User user = validateInterceptor(token);

        if (!(user instanceof Admin)) {
            throw new UnauthorizedException("Not authorized. Must be admin.");
        }

        // TODO: Cerca tutti gli eventi di qui l'utente è admin e ritorna la lista

        return eventDAO.getAllEvents();
    }

    public void addStaff(AddStaffToEventDTO dto, String token) {

        User user = validateInterceptor(token);

        if (!(user instanceof Admin)) {
            throw new UnauthorizedException("Not authorized. Must be admin.");
        }

        // cerco l'user dato l'email nel dto e creo lo staff.

        User dbUser = userDAO.findUserByEmail(dto.getStaffEmail());

        if (dbUser == null) {
            throw new NotFoundException("User not found.");
        }

        // TODO: aggiungere lo staff all'evento dato l'id dell'evento

    }

    public void removeStaff(RemoveStaffFromEventDTO dto, String token) {
        User user = validateInterceptor(token);

        if (!(user instanceof Admin)) {
            throw new UnauthorizedException("Not authorized. Must be admin.");
        }

        User dbUser = userDAO.findUserByEmail(dto.getStaffEmail());

        if (dbUser == null) {
            throw new NotFoundException("User not found.");

        }
    }

    public void setEventDAO(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

}
