package org.swe;

import org.swe.core.Config;
import org.swe.core.orm.DBManager;

public class Main {
    public static void main(String[] args) {
        // Load the configurations
        Config.init();
        // DB connection test
        System.out.println("DB connection test");

        DBManager dbManager = DBManager.getInstance();
        if (dbManager.getConnection() != null) {
            System.out.println("Connection established");
        } else {
            System.out.println("Connection failed");
        }
        DBManager.close();
    }
}