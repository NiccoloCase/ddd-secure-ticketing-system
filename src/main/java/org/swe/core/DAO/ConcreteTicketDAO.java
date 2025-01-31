package org.swe.core.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.swe.core.DBM.DBManager;
import org.swe.model.Ticket;

public class ConcreteTicketDAO implements TicketDAO {
     private DBManager dbManager;

     public ConcreteTicketDAO() {
          dbManager = DBManager.getInstance();
     }

     @Override
     public Ticket getTicketById(int id) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ticket WHERE id = ?");
               statement.setInt(1, id);
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                    Ticket ticket = new Ticket(resultSet.getInt("id"), resultSet.getInt("user_id"),
                              resultSet.getInt("event_id"), resultSet.getInt("quantity"), resultSet.getBoolean("used"));
                    statement.close();
                    return ticket;
               }
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }

     @Override
     public ArrayList<Ticket> getAllTickets() {
          ArrayList<Ticket> tickets = new ArrayList<>();
          try {
               Connection connection = dbManager.getConnection();
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery("SELECT * FROM Ticket");
               while (resultSet.next()) {
                    tickets.add(new Ticket(resultSet.getInt("id"), resultSet.getInt("user_id"),
                              resultSet.getInt("event_id"), resultSet.getInt("quantity"),
                              resultSet.getBoolean("used")));
               }
               statement.close();
               return tickets;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }

     @Override
     public Ticket createTicket(Integer userId, Integer eventId, Integer quantity) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO Ticket (user_id, event_id, quantity, used) VALUES (?, ?, ?, ?)",
                         Statement.RETURN_GENERATED_KEYS);

               statement.setInt(1, userId);
               statement.setInt(2, eventId);
               statement.setInt(3, quantity);
               statement.setBoolean(4, false);

               int affectedRows = statement.executeUpdate();

               if (affectedRows == 0) {
                    statement.close();
                    return null;
               }

               ResultSet resultSet = statement.getGeneratedKeys();
               if (resultSet.next()) {
                    int ticketId = resultSet.getInt(1);
                    Ticket ticket = new Ticket(ticketId, userId, eventId, quantity, false);

                    resultSet.close();
                    statement.close();
                    return ticket;
               }

               resultSet.close();
               statement.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }

     @Override
     public boolean setTicketUsed(Integer ticketId) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("UPDATE Ticket SET used = ? WHERE id = ?");
               statement.setBoolean(1, true);
               statement.setInt(2, ticketId);
               statement.executeUpdate();
               statement.close();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }

     @Override
     public boolean deleteTicket(int id) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("DELETE FROM Ticket WHERE id = ?");
               statement.setInt(1, id);
               statement.executeUpdate();
               statement.close();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }

     @Override
     public ArrayList<Ticket> getTicketsByUserAndEvent(Integer userId, Integer eventId) {
          ArrayList<Ticket> tickets = new ArrayList<>();
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection
                         .prepareStatement("SELECT * FROM Ticket WHERE user_id = ? AND event_id = ?");
               statement.setInt(1, userId);
               statement.setInt(2, eventId);
               ResultSet resultSet = statement.executeQuery();
               while (resultSet.next()) {
                    tickets.add(new Ticket(resultSet.getInt("id"), resultSet.getInt("user_id"),
                              resultSet.getInt("event_id"), resultSet.getInt("quantity"),
                              resultSet.getBoolean("used")));
               }
               statement.close();
               return tickets;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }
}
