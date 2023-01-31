package net.artelnatif.nicko;

import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.config.ConfigurationManager;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.storage.PlayerDataStore;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Nicko {
    private boolean bungeecord = false;
    private ConfigurationManager configManager;
    private Logger logger;
    private File dataFolder;
    private MojangAPI mojangAPI;
    private Configuration config;

    private PlayerDataStore dataStore;

    public void initBungeecord(Plugin bungee) {
        logger = bungee.getLogger();
        dataFolder = bungee.getDataFolder();
        bungeecord = true;
        initNicko();
    }

    public void initBukkit(JavaPlugin bukkit) {
        logger = bukkit.getLogger();
        dataFolder = bukkit.getDataFolder();
        initNicko();
    }

    private void initNicko() {
        configManager = new ConfigurationManager(this);
        configManager.saveDefaultConfig();

        mojangAPI = new MojangAPI(this);
        dataStore = new PlayerDataStore(this);
    }

    public Logger getLogger() {
        return logger;
    }

    public PlayerDataStore getDataStore() {
        return dataStore;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public MojangAPI getMojangAPI() {
        return mojangAPI;
    }

    public ConfigurationManager getConfigManager() {
        return configManager;
    }

    public Configuration getConfig() {
        try {
            if (config == null) { return config = configManager.load(); }
            return config;
        } catch (IOException e) {
            logger.severe("Failed to load configuration file: " + e.getMessage());
            return null;
        }
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public boolean isBungeecord() {
        return bungeecord;
    }

    public void setBungeecord(boolean bungeecord) {
        this.bungeecord = bungeecord;
    }
}
