package org.swe;
import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    public static String JWT_SECRET;
    public static String DB_URL;

    public static void init() {
        Dotenv dotenv = Dotenv.load();
        JWT_SECRET = dotenv.get("JWT_SECRET");
        DB_URL = dotenv.get("DB_URL");
    }

}
