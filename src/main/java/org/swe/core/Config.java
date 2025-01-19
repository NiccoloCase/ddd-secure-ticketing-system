package org.swe.core;
import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    public static String JWT_SECRET;
    public static String DB_URL;



    static {
        try {
            Dotenv dotenv = Dotenv.load();
            JWT_SECRET = dotenv.get("JWT_SECRET");
            DB_URL = dotenv.get("DB_URL");
        } catch (Exception e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
    }

}
