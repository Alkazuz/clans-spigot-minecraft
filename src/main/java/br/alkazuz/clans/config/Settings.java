package br.alkazuz.clans.config;

import br.alkazuz.clans.config.manager.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings {

    public static boolean MYSQL_ENABLED;
    public static String MYSQL_HOST;
    public static String MYSQL_DATABASE;
    public static String MYSQL_USER;
    public static String MYSQL_PASSWORD;

    public static void load() {
        try {
            FileConfiguration config = ConfigManager.getConfig("settings");
            MYSQL_ENABLED = config.getBoolean("MySQL.enabled");
            MYSQL_HOST = config.getString("MySQL.host");
            MYSQL_DATABASE = config.getString("MySQL.database");
            MYSQL_USER = config.getString("MySQL.user");
            MYSQL_PASSWORD = config.getString("MySQL.password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
