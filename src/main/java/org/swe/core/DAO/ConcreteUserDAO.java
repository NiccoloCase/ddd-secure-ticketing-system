package org.swe.core.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.swe.core.dbManager.DBManager;
import org.swe.model.User;

//i campi sono degli esempi andranno quasi certamente modificati con quelli reali 

public class ConcreteUserDAO implements UserDAO {
     private DBManager dbManager;

     public ConcreteUserDAO() {
          dbManager = DBManager.getInstance();
     }

     public User getUser(int id) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM guests WHERE id = ?");
               statement.setInt(1, id);
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                    return new User(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("passwordHash"), resultSet.getString("email"), resultSet.getInt("id"));
               }
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }

     public ArrayList<User> getAllUsers() {
          ArrayList<User> guests = new ArrayList<>();
          try {
               Connection connection = dbManager.getConnection();
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery("SELECT * FROM guests");
               while (resultSet.next()) {
                    guests.add(new User(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("passwordHash"), resultSet.getString("email"), resultSet.getInt("id")));
               }
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return guests;
     }

     public User findUserByEmail(String email) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("SELECT * FROM guests WHERE email = ?");
               statement.setString(1, email);
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                    return new User(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("passwordHash"), resultSet.getString("email"), resultSet.getInt("id"));
               }
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return null;
     }

     public boolean addUser(User guest) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("INSERT INTO guests (name, email) VALUES (?, ?)");
               statement.setString(1, guest.getName());
               statement.setString(2, guest.getEmail());
               statement.executeUpdate();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }

     public boolean updateUser(User guest) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("UPDATE guests SET name = ?, email = ? WHERE id = ?");
               statement.setString(1, guest.getName());
               statement.setString(2, guest.getEmail());
               statement.setInt(3, guest.getId());
               statement.executeUpdate();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }

     public boolean deleteUser(int id) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement("DELETE FROM guests WHERE id = ?");
               statement.setInt(1, id);
               statement.executeUpdate();
               return true;
          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }
     
}
