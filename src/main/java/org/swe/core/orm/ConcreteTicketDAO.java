package org.swe.core.orm;

import org.swe.model.Ticket;
import java.util.ArrayList;
import java.sql.*;

public class ConcreteTicketDAO implements TicketDAO {
    private DBManager dbManager;

    public ConcreteTicketDAO() {
        dbManager = DBManager.getInstance();
     }

    public Ticket getTicketById(int id) {
        try {
            Connection connection = dbManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tickets WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Ticket(resultSet.getInt("id"), resultSet.getInt("userId"), resultSet.getInt("quantity"), resultSet.getInt("code"), resultSet.getBoolean("used"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return null;
     }

     public ArrayList<Ticket> getAllTickets() {
          ArrayList<Ticket> tickets = new ArrayList<>();
          try {
               Connection connection = dbManager.getConnection();
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery("SELECT * FROM tickets");
               while (resultSet.next()) {
                    tickets.add(new Ticket(resultSet.getInt("id"), resultSet.getInt("userId"), resultSet.getInt("quantity"), resultSet.getInt("code"), resultSet.getBoolean("used")));
               }
               return tickets;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }

     public Ticket findTicketByCode(int code) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM tickets WHERE code = ?");
               statement.setInt(1, code);
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                    return new Ticket(resultSet.getInt("id"), resultSet.getInt("userId"), resultSet.getInt("quantity"), resultSet.getInt("code"), resultSet.getBoolean("used"));
               }
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }

     public boolean addTicket(Ticket ticket) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("INSERT INTO tickets (id, userId, quantity, code, used) VALUES (?, ?, ?, ?, ?)");
               statement.setInt(1, ticket.getId());
               statement.setInt(2, ticket.getUserId());
               statement.setInt(3, ticket.getQuantity());
               statement.setInt(4, ticket.getCode());
               statement.setBoolean(5, ticket.isUsed());
               statement.executeUpdate();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }

     public boolean updateTicket(Ticket ticket) {
          try {
                  Connection connection = dbManager.getConnection();
                  PreparedStatement statement = connection.prepareStatement("UPDATE tickets SET userId = ?, used = ? WHERE id = ?");
                  statement.setInt(1, ticket.getUserId());
                  statement.setBoolean(2, ticket.isUsed());
                  statement.setInt(3, ticket.getId());
                  statement.executeUpdate();
                  return true;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }

     public boolean deleteTicket(int id) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("DELETE FROM tickets WHERE id = ?");
               statement.setInt(1, id);
               statement.executeUpdate();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }
}


