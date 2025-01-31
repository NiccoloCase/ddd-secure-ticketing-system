package org.swe.core.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.swe.core.DBM.DBManager;
import org.swe.model.Admin;
import org.swe.model.Event;
import org.swe.model.Staff;

public class ConcreteEventDAO implements EventDAO {

     private DBManager dbManager;
     private StaffDAO staffDAO;
     private AdminDAO adminDAO;

     public ConcreteEventDAO() {
          this.dbManager = DBManager.getInstance();
          this.staffDAO = new ConcreteStaffDAO();
          this.adminDAO = new ConcreteAdminDAO();
     }

     @Override
     public Event getEventById(int id) {
          Event event = null;
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement(
                         "SELECT * FROM Event WHERE id = ?");
               statement.setInt(1, id);

               ResultSet rs = statement.executeQuery();
               if (rs.next()) {
                    Event.Builder builder = new Event.Builder()
                              .setId(rs.getInt("id"))
                              .setTitle(rs.getString("title"))
                              .setDescription(rs.getString("description"))
                              .setDate(rs.getTimestamp("date"))
                              .setTicketsAvailable(rs.getInt("tickets_available"))
                              .setTicketPrice(rs.getDouble("ticket_price"));


                    List<Staff> staffList = staffDAO.getStaffByEventId(id);
                    List<Admin> adminList = adminDAO.getAdminsByEventId(id);

                    builder.setStaff(staffList);
                    builder.setAdmins(adminList);

                    event = builder.build();
               }
               rs.close();
               statement.close();
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

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return events;
     }

     @Override
     public Event createEvent(String title, String description, Date date, int ticketsAvailable, double ticketPrice) {
          Event event = null;
          try {
               Connection connection = dbManager.getConnection();

               PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO Event (title, description, date, tickets_available, ticket_price) VALUES (?, ?, ?, ?, ?)",
                         Statement.RETURN_GENERATED_KEYS);
               statement.setString(1, title);
               statement.setString(2, description);
               statement.setDate(3, new java.sql.Date(date.getTime()));
               statement.setInt(4, ticketsAvailable);
               statement.setDouble(5, ticketPrice);

               int rowsInserted = statement.executeUpdate();
               if (rowsInserted > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                         if (generatedKeys.next()) {
                              int newId = generatedKeys.getInt(1);
                              event = new Event.Builder()
                                        .setId(newId)
                                        .setTitle(title)
                                        .setDescription(description)
                                        .setDate(date)
                                        .setTicketsAvailable(ticketsAvailable)
                                        .setTicketPrice(ticketPrice)
                                        .build();
                         }
                    }
               }
               statement.close();

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return event;
     }

     @Override
     public boolean updateEvent(int id, String title, String description, Date date, int ticketsAvailable,
               double ticketPrice) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement(
                         "UPDATE Event SET title = ?, description = ?, date = ?, tickets_available = ?, ticket_price = ? WHERE id = ?");
               statement.setString(1, title);
               statement.setString(2, description);
               statement.setDate(3, new java.sql.Date(date.getTime()));
               statement.setInt(4, ticketsAvailable);
               statement.setDouble(5, ticketPrice);
               statement.setInt(6, id);

               int rowsUpdated = statement.executeUpdate();
               statement.close();
               return rowsUpdated > 0;

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }

     @Override
     public boolean isUserAdminOfEvent(int userId, int eventId) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement(
                         "SELECT * FROM Admin WHERE user_id = ? AND event_id = ?");
               statement.setInt(1, userId);
               statement.setInt(2, eventId);

               ResultSet rs = statement.executeQuery();
               boolean isAdmin = rs.next();
               rs.close();
               statement.close();
               return isAdmin;

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }

     @Override
     public boolean deleteEvent(int id) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement(
                         "DELETE FROM Event WHERE id = ?");
               statement.setInt(1, id);

               int rowsDeleted = statement.executeUpdate();
               statement.close();
               return rowsDeleted > 0;

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }
}