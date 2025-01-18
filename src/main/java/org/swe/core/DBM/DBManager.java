package org.swe.core.DBM;

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
}