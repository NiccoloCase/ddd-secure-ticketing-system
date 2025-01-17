package org.swe.core.DAO;

import org.swe.model.Ticket;
import java.util.ArrayList;

public interface TicketDAO {
     Ticket getTicketById(int id);
     ArrayList<Ticket> getAllTickets();
     Ticket findTicketByCode(int code);
     boolean addTicket(Ticket ticket);
     boolean updateTicket(Ticket ticket);
     boolean deleteTicket(int id);     
 }