package org.swe.core.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.swe.core.DBM.DBManager;
import org.swe.model.Admin;
import org.swe.model.Event;

public class ConcreteAdminDAO implements AdminDAO {

    private DBManager dbManager = DBManager.getInstance();
    

    @Override
    public boolean addAdminToEvent(int userId, int eventId) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO Admin (user_id, event_id) VALUES (?, ?)"
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
    public boolean removeAdminFromEvent(int userId, int eventId) {
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM Admin WHERE user_id = ? AND event_id = ?"
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
    public List<Admin> getAdminsByEventId(int eventId) {
        List<Admin> adminList = new ArrayList<>();
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT u.id AS user_id, u.name, u.surname, u.email, u.password_hash, a.event_id " +
                "FROM Admin a " +
                "JOIN AppUser u ON a.user_id = u.id " +
                "WHERE a.event_id = ?"
            );
            stmt.setInt(1, eventId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Admin admin = new Admin(
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("password_hash"),
                    rs.getString("email"),
                    rs.getInt("user_id"),   
                    rs.getInt("event_id")
                );
                adminList.add(admin);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    @Override
    public List<Event> getEventsByAdminUserId(int userId) {
        List<Event> events = new ArrayList<>();
        try {
            Connection conn = dbManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT e.* " +
                "FROM Admin a " +
                "JOIN Event e ON a.event_id = e.id " +
                "WHERE a.user_id = ?"
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