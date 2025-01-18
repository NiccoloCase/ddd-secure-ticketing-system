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
                return new Ticket(resultSet.getInt("id"), resultSet.getInt("user_id"),  resultSet.getInt("event_id"), resultSet.getInt("quantity"), resultSet.getBoolean("used"));
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
                    tickets.add(new Ticket(resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getInt("event_id"), resultSet.getInt("quantity"), resultSet.getBoolean("used")));
               }
               return tickets;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }

    @Override
     public Ticket findTicketByCode(int code) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM Ticket WHERE code = ?");
               statement.setInt(1, code);
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                    return new Ticket(resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getInt("event_id "), resultSet.getInt("quantity"), resultSet.getBoolean("used"));
               }
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }

    @Override
     public Ticket createTicket(Integer userId, Integer eventId, Integer quantity) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("INSERT INTO Ticket (user_id, event_id, quantity, used) VALUES (?, ?, ?, ?)");
               statement.setInt(1, userId);
                statement.setInt(2, eventId);
               statement.setInt(3, quantity);
               statement.setBoolean(4, false);
               statement.executeUpdate();

                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                      return new Ticket(resultSet.getInt(1), userId, eventId, quantity, false);
                }
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
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }
}


