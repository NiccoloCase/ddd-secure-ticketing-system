package org.swe.core.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.swe.core.DBM.DBManager;
import org.swe.core.utils.PasswordUtility;
import org.swe.model.User;

public class ConcreteUserDAO implements UserDAO {

     private DBManager dbManager;

     public ConcreteUserDAO() {
          this.dbManager = DBManager.getInstance();
     }

     @Override
     public User getUserById(int id) {
          User user = null;
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement(
                         "SELECT * FROM guests WHERE id = ?");
               statement.setInt(1, id);

               ResultSet rs = statement.executeQuery();
               if (rs.next()) {
                    user = new User(
                              rs.getString("name"),
                              rs.getString("surname"),
                              rs.getString("passwordHash"),
                              rs.getString("email"),
                              rs.getInt("id"));
               }
               rs.close();
               statement.close();

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return user;
     }

     @Override
     public ArrayList<User> getAllUsers() {
          ArrayList<User> users = new ArrayList<>();
          try {
               Connection connection = dbManager.getConnection();
               Statement statement = connection.createStatement();
               ResultSet rs = statement.executeQuery("SELECT * FROM guests");

               while (rs.next()) {
                    User user = new User(
                              rs.getString("name"),
                              rs.getString("surname"),
                              rs.getString("passwordHash"),
                              rs.getString("email"),
                              rs.getInt("id"));
                    users.add(user);
               }
               rs.close();
               statement.close();

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return users;
     }

     @Override
     public User getUserByEmail(String email) {
          User user = null;
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement(
                         "SELECT * FROM guests WHERE email = ?");
               statement.setString(1, email);

               ResultSet rs = statement.executeQuery();
               if (rs.next()) {
                    user = new User(
                              rs.getString("name"),
                              rs.getString("surname"),
                              rs.getString("passwordHash"),
                              rs.getString("email"),
                              rs.getInt("id"));
               }
               rs.close();
               statement.close();

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return user;
     }

     @Override
     public User createUser(String name, String surname, String clearPassword, String email) {
          User newUser = null;
          String passwordHash = User.hashPassword(clearPassword);

          try {
               Connection connection = dbManager.getConnection();

               PreparedStatement statement = connection.prepareStatement(
                         "INSERT INTO guests (name, surname, passwordHash, email) VALUES (?, ?, ?, ?)",
                         Statement.RETURN_GENERATED_KEYS);
               statement.setString(1, name);
               statement.setString(2, surname);
               statement.setString(3, passwordHash);
               statement.setString(4, email);

               int rowsInserted = statement.executeUpdate();
               if (rowsInserted > 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                         if (generatedKeys.next()) {
                              int newId = generatedKeys.getInt(1);
                              newUser = new User(name, surname, passwordHash, email, newId);
                         }
                    }
               }
               statement.close();

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return newUser;
     }

     @Override
     public boolean updateUser(int id, String name, String surname, String passwordHash, String email) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement(
                         "UPDATE guests SET name = ?, surname = ?, passwordHash = ?, email = ? WHERE id = ?");
               statement.setString(1, name);
               statement.setString(2, surname);
               statement.setString(3, passwordHash);
               statement.setString(4, email);
               statement.setInt(5, id);

               int rowsUpdated = statement.executeUpdate();
               statement.close();
               return (rowsUpdated > 0);

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }

     @Override
     public boolean deleteUser(int id) {
          try {
               Connection connection = dbManager.getConnection();
               PreparedStatement statement = connection.prepareStatement(
                         "DELETE FROM guests WHERE id = ?");
               statement.setInt(1, id);

               int rowsDeleted = statement.executeUpdate();
               statement.close();
               return (rowsDeleted > 0);

          } catch (SQLException e) {
               e.printStackTrace();
          }
          return false;
     }
}