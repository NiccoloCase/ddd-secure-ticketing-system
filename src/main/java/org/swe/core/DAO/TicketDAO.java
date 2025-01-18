package org.swe.core.DAO;

import java.util.ArrayList;

import org.swe.model.Ticket;

public interface TicketDAO {
     Ticket getTicketById(int id);
     ArrayList<Ticket> getAllTickets();
     Ticket createTicket(Integer userId, Integer eventId, Integer quantity);
     boolean setTicketUsed(Integer ticketId);
     boolean deleteTicket(int id);    
     ArrayList<Ticket> getTicketsByUserAndEvent(Integer userId, Integer eventId);
 }