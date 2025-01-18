package org.swe.core.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.swe.core.DBM.DBManager;
import org.swe.model.Event;
import org.swe.model.Staff;

public class ConcreteStaffDAO implements StaffDAO {

    private DBManager dbManager = DBManager.getInstance();

    @Override
    public boolean addStaffToEvent(int userId, int eventId) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Staff (user_id, event_id) VALUES (?, ?)"
            );
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);

            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeStaffFromEvent(int userId, int eventId) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Staff WHERE user_id = ? AND event_id = ?"
            );
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);
            
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Staff> getStaffByEventId(int eventId) {
        List<Staff> staffList = new ArrayList<>();
        try {
            Connection conn = dbManager.getConnection();
            
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT u.id AS user_id, u.name, u.surname, u.email, u.password_hash, s.event_id " +
                "FROM Staff s " +
                "JOIN AppUser u ON s.user_id = u.id " +
                "WHERE s.event_id = ?"
            );
            stmt.setInt(1, eventId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Staff staff = new Staff(
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("password_hash"),
                    rs.getString("email"),
                    rs.getInt("user_id"),  
                    rs.getInt("event_id")
                );
                staffList.add(staff);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    @Override
    public List<Event> getEventsByStaffUserId(int userId) {
        List<Event> events = new ArrayList<>();
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT e.* " +
                "FROM Staff s " +
                "JOIN Event e ON s.event_id = e.id " +
                "WHERE s.user_id = ?"
            );
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Event event = new Event.Builder()
                    .setId(rs.getInt("id"))
                    .setTitle(rs.getString("title"))
                    .setDescription(rs.getString("description"))
                    .setDate(rs.getTimestamp("date"))
                    .setTicketsAvailable(rs.getInt("tickets_available"))
                    .setTicketPrice(rs.getDouble("ticket_price"))
                    .build();
                events.add(event);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}