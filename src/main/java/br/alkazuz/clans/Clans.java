package br.alkazuz.clans;

import br.alkazuz.clans.command.CommandChatClan;
import br.alkazuz.clans.command.CommandClan;
import br.alkazuz.clans.command.SubCommandBase;
import br.alkazuz.clans.command.SubCommands;
import br.alkazuz.clans.config.Settings;
import br.alkazuz.clans.config.manager.ConfigManager;
import br.alkazuz.clans.gui.listener.InventoryClickListener;
import br.alkazuz.clans.manager.ClanManager;
import br.alkazuz.clans.manager.ClanPlayerManager;
import br.alkazuz.clans.storage.DBCore;
import br.alkazuz.clans.storage.MySQLCore;
import br.alkazuz.clans.storage.SQLiteCore;
import br.alkazuz.clans.utils.EventWaiter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Clans extends JavaPlugin {
    public DBCore database;
    private Economy economy;
    private EventWaiter eventWaiter;
    private static Clans instance;

    public static Clans getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;
        genSetings();
        loadSettings();
        getCommand("clan").setExecutor(new CommandClan());
        getCommand(".").setExecutor(new CommandChatClan());
        setupVaultEconomy();
        eventWaiter = new EventWaiter(this);
        eventWaiter.addEvents(PlayerChatEvent.class, InventoryClickEvent.class);
        registerEvents();
        startDatabase();
        SubCommands.load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void genSetings() {
        ConfigManager.createConfig("settings");
    }

    public void loadSettings() {
        Settings.load();
    }

    private void startDatabase() {
        if (Settings.MYSQL_ENABLED) {
            database = new MySQLCore(Settings.MYSQL_HOST, Settings.MYSQL_DATABASE, Settings.MYSQL_USER, Settings.MYSQL_PASSWORD);
        } else {
            database = new SQLiteCore(getDataFolder().getPath());
        }
        if (database.checkConnection().booleanValue()) {
            database.execute("CREATE TABLE IF NOT EXISTS `clans` (" +
                    "`id` INT NOT NULL AUTO_INCREMENT," +
                    "`name` VARCHAR(32) NOT NULL," +
                    "`tag` VARCHAR(32) NOT NULL," +
                    "`color` VARCHAR(32) NOT NULL," +
                    "`owner` VARCHAR(32) NOT NULL," +
                    "`home` VARCHAR(255)," +
                    "`balance` DOUBLE NOT NULL," +
                    "`created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "`friendly_fire` BOOLEAN NOT NULL DEFAULT FALSE," +
                    "`points` INT NOT NULL," +
                    "`warnings` INT NOT NULL DEFAULT 0," +
                    "PRIMARY KEY (`id`));");
            database.execute("CREATE TABLE IF NOT EXISTS `clans_players` (" +
                    "`id` INT AUTO_INCREMENT PRIMARY KEY," +
                    "`name` VARCHAR(32) NOT NULL," +
                    "`clan_id` INT," +
                    "`role` VARCHAR(32) NOT NULL," +
                    "`kills` INT NOT NULL," +
                    "`deaths` INT NOT NULL," +
                    "`last_online` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (`clan_id`) REFERENCES `clans`(`id`) ON DELETE SET NULL);");
        }

        ClanManager.loadAll();
        ClanPlayerManager.loadAll();
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new InventoryClickListener(), this);
    }
    public Economy getEconomy() {
        return economy;
    }

    private void setupVaultEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            debug("Vault não encontrado, desabilitando economia.");
            return;
        }
        economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
        if (economy == null) {
            debug("Vault encontrado, mas não foi possível encontrar um plugin de economia.");
        }
    }

    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }

    public static void debug(String message) {
        Bukkit.getConsoleSender().sendMessage("§e[Clans] " + message);
    }
}
