package org.swe.core.orm;

import org.swe.model.Event;
import java.sql.*;
import java.util.ArrayList;

public class EventDAO {
     private DBManager dbManager;

     public EventDAO() {
          dbManager = DBManager.getInstance();
     }

     public Event getEvent(int id, String title) {
          Event event = null;
          try {
               Connection connection = dbManager.getConnection();
               String query = "SELECT * FROM events WHERE id = ? AND name = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setInt(1, id);
               statement.setString(2, title);
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                    event = new Event.Builder()
                         .setId(resultSet.getInt("id"))
                         .setTitle(resultSet.getString("title"))
                         .setDescription(resultSet.getString("description"))
                         .setDate(resultSet.getDate("date"))
                         .setTicketsAvailable(resultSet.getInt("ticketsAvailable"))
                         .setTicketPrice(resultSet.getDouble("ticketPrice"))
                         .build();
               }
               resultSet.close();
               statement.close();
               connection.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return event;
     }

     public ArrayList<Event> getAllEvents() {
          ArrayList<Event> events = new ArrayList<>();
          try {
               Connection connection = dbManager.getConnection();
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery("SELECT * FROM events");
               while (resultSet.next()) {
                    Event event = new Event.Builder()
                         .setId(resultSet.getInt("id"))
                         .setTitle(resultSet.getString("title"))
                         .setDescription(resultSet.getString("description"))
                         .setDate(resultSet.getDate("date"))
                         .setTicketsAvailable(resultSet.getInt("ticketsAvailable"))
                         .setTicketPrice(resultSet.getDouble("ticketPrice"))
                         .build();
                    events.add(event);
               }
               resultSet.close();
               statement.close();
               connection.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return events;
     }

     public void createEvent(Event event) {
          try {
               Connection connection = dbManager.getConnection();
               String query = "INSERT INTO events (id, name, ...) VALUES (?, ?, ...)";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setInt(1, event.getId());
               statement.setString(2, event.getTitle());
               statement.executeUpdate();
               statement.close();
               connection.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }

     public void updateEvent(Event event) {
          try {
               Connection connection = dbManager.getConnection();
               String query = "UPDATE events SET name = ?, ... WHERE id = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setString(1, event.getTitle());
               statement.setString(2, event.getDescription());
               statement.setDate(3, new java.sql.Date(event.getDate().getTime()));
               statement.setInt(4, event.getTicketsAvailable());
               statement.setDouble(5, event.getTicketPrice());
               statement.setInt(2, event.getId());
               statement.executeUpdate();
               statement.close();
               connection.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }

     public void deleteEvent(int id, String title) {
          try {
               Connection connection = dbManager.getConnection();
               String query = "DELETE FROM events WHERE id = ? AND name = ?";
               PreparedStatement statement = connection.prepareStatement(query);
               statement.setInt(1, id);
               statement.setString(2, title);
               statement.executeUpdate();
               statement.close();
               connection.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }
}
