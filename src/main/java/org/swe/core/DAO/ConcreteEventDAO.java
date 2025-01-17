package org.swe.core.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.swe.core.dbManager.DBManager;
import org.swe.model.Event;

public class ConcreteEventDAO implements EventDAO {
     private DBManager dbManager;

     public ConcreteEventDAO() {
          dbManager = DBManager.getInstance();
     }

     @Override
     public Event getEvent(int id) {
          Event event = null;
          try {
               Connection connection = dbManager.getConnection();
               String query = "SELECT * FROM Event WHERE id = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setInt(1, id);
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                    event = new Event.Builder()
                         .setId(resultSet.getInt("id"))
                         .setTitle(resultSet.getString("title"))
                         .setDescription(resultSet.getString("description"))
                         .setDate(resultSet.getDate("date"))
                         .setTicketsAvailable(resultSet.getInt("tickets_available"))
                         .setTicketPrice(resultSet.getDouble("ticket_price"))
                         .build();
               }
               resultSet.close();
               statement.close();
               //connection.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return event;
     }

     @Override
     public ArrayList<Event> getAllEvents() {
          ArrayList<Event> events = new ArrayList<>();
          try {
               Connection connection = dbManager.getConnection();
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery("SELECT * FROM Event");
               while (resultSet.next()) {
                    Event event = new Event.Builder()
                         .setId(resultSet.getInt("id"))
                         .setTitle(resultSet.getString("title"))
                         .setDescription(resultSet.getString("description"))
                         .setDate(resultSet.getDate("date"))
                         .setTicketsAvailable(resultSet.getInt("tickets_available"))
                         .setTicketPrice(resultSet.getDouble("ticket_price"))
                         .build();
                    events.add(event);
               }
               resultSet.close();
               statement.close();
               //connection.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return events;
     }

     @Override
     public boolean addEvent(Event event) {
          try {
               Connection connection = dbManager.getConnection();
               String query = "INSERT INTO Event (id, title, description, date, tickets_available, ticket_price) VALUES (?, ?, ?, ?, ?, ?)";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setInt(1, event.getId());
               statement.setString(2, event.getTitle());
               statement.setString(3, event.getDescription());
               statement.setDate(4, new java.sql.Date(event.getDate().getTime()));
               statement.setInt(5, event.getTicketsAvailable());
               statement.setDouble(6, event.getTicketPrice());
               statement.executeUpdate();
               statement.close();
               //connection.close();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
               return false;
          }
     }

     @Override
     public boolean updateEvent(Event event) {
          try {
               Connection connection = dbManager.getConnection();
               String query = "UPDATE Event SET title = ?, description = ?, date = ?, tickets_available = ?, ticket_price = ?  WHERE id = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setString(1, event.getTitle());
               statement.setString(2, event.getDescription());
               statement.setDate(3, new java.sql.Date(event.getDate().getTime()));
               statement.setInt(4, event.getTicketsAvailable());
               statement.setDouble(5, event.getTicketPrice());
               statement.setInt(6, event.getId());
               statement.executeUpdate();
               statement.close();
               //connection.close();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
               return false;
          }
     }

     @Override
     public boolean deleteEvent(int id) {
          try {
               Connection connection = dbManager.getConnection();
               String query = "DELETE FROM Event WHERE id = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setInt(1, id);
               statement.executeUpdate();
               statement.close();
               //connection.close();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
               return false;
          }
     }
}
