package org.swe.core.DAO;

import org.swe.model.Ticket;
import java.util.ArrayList;

public interface TicketDAO {
     Ticket getTicketById(int id);
     ArrayList<Ticket> getAllTickets();
     Ticket findTicketByCode(int code);
     Ticket createTicket(Integer userId, Integer eventId, Integer quantity);
     boolean setTicketUsed(Integer ticketId);
     boolean deleteTicket(int id);     
 }