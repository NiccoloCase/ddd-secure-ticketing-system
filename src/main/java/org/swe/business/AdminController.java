package org.swe.business;

import java.util.Date;
import java.util.List;

import org.swe.model.Event;

public class AdminController extends UserController {

    public AdminController(AuthService authHandler) {
        super(authHandler);
    }

    public Event createEvent() {

        // TODO: Verifica token?


        // TODO: Salvataggio nel database
        // DBManager.save(event);

        return null;
    }

    public void deleteEvent(int eventId) {
        // // Event event = DBManager.getEvent(eventId);
        // if (event != null) {
        // // DBManager.delete(event);
        // }
        // else {
        // throw new IllegalArgumentException("Event not found");
        // }
    }

    public void updateEvent(int eventId, String title, String description, Date date, int ticketsAvailable,
            double ticketPrice) {
        // Event event = DBManager.getEvent(eventId);
        // if (event != null) {
        // event.setTitle(title);
        // event.setDescription(description);
        // event.setDate(date);
        // event.setTicketsAvailable(ticketsAvailable);
        // event.setTicketPrice(ticketPrice);
        // // DBManager.update(event);
        // }
        // else {
        // throw new IllegalArgumentException("Event not found");
        // }
    }

    public List<Event> getEvents() {
        // return DBManager.getEvents();
        return null;
    }

    public void addStaff(int eventId, int userId) {
        // Event event = DBManager.getEvent(eventId);
        // User user = DBManager.getUser(userId);
        // if (user instanceof Staff) {
        // TODO: aggiungo lo staff all'evento o l'evento allo staff?
        // }
        // else {
        // throw new IllegalArgumentException("User is not a staff member");
        // }
    }

    public void removeStaff(int eventId, int userId) {
        // Event event = DBManager.getEvent(eventId);
        // User user = DBManager.getUser(userId);
        // if (user instanceof Staff) {
        // TODO: rimuovo lo staff dall'evento o l'evento dallo staff?
        // }
        // TODO: Gestire caso in cui l'utente non è uno staff oppure non fa parte
        // dell'evento
    }

}
