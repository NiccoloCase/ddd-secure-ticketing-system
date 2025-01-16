package org.swe.business;

import org.swe.model.Ticket;

public class GuestController extends UserController {

    public GuestController(AuthService authHandler) {
        super(authHandler);
    }

    public Ticket buyTicket(int eventId) {
        // TODO: prendere l'evento dal DB, decrementare i biglietti disponibili
        // creare nuovo ticket e salvarlo nel DB
        // return ticket
        return null;
    }

    public boolean proveTicketValidity(int ticketId, int eventId) {
        // TODO: user scanneriza il ticket, vediamo se è valido per l'evento e l'utente
        return true;
    }

}
