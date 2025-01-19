package org.swe.core.DBM;

import org.swe.Config;

import java.sql.*;


public class DBManager {

     private static DBManager iManager = null;
     private Connection connection = null;

     private DBManager() {
           try {
               this.connection = DriverManager.getConnection(Config.DB_URL);
               System.out.println("Connection established");
           } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Connection failed");
           }
     }

     private static DBManager init() {
           if (iManager == null) {
                iManager = new DBManager();
           }
           return iManager;
     }

     public static DBManager getInstance() {
           return init();
     }

     public Connection getConnection() {
          return connection;
    }

     public void close() {
           try {
                if (iManager != null && iManager.connection != null && !iManager.connection.isClosed()) {
                     iManager.connection.close();
                     System.out.println("Connection closed");
                     iManager = null;
                }
           } catch (SQLException e) {
                e.printStackTrace();
           }
     }

    public void clearTables() {
        String[] tables = {"appuser", "event", "staff", "ticket", "admin"};

        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false); // Begin transaction.

            for (String table : tables) {
                statement.executeUpdate("DELETE FROM " + table);
            }
            connection.commit();
            System.out.println("All tables cleared successfully.");
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback in case of error.
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback failed: " + rollbackEx.getMessage());
            }
            System.err.println("Failed to clear tables: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true); // Restore default auto-commit mode.
            } catch (SQLException ex) {
                System.err.println("Failed to restore auto-commit mode: " + ex.getMessage());
            }
        }
    }



}