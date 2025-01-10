package org.swe.helpers;
import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    public static String JWT_SECRET;

    public static void init() {
        Dotenv dotenv = Dotenv.load();
        JWT_SECRET = dotenv.get("JWT_SECRET");
        System.out.println("Configurations loaded");
        System.out.println("JWT_SECRET: " + JWT_SECRET);
    }

}
