package org.swe.core.orm;

import org.swe.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DBManager {
     private static DBManager iManager = null;
     private Connection connection = null;

     private DBManager() {
          Config.init();
           try {
                connection = DriverManager.getConnection(Config.DB_URL);
               System.out.println("Connection established");
           } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Connection failed");
           }
     }

     public static DBManager getInstance() {
           if (iManager == null) {
                iManager = new DBManager();
           }
           return iManager;
     }

     public static void close() {
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

     public Connection getConnection() {
           return connection;
     }
}
