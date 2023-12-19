package xyz.ineanto.nicko;

import com.comphenix.protocol.utility.MinecraftVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.ineanto.nicko.command.NickoCommand;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.ConfigurationManager;
import xyz.ineanto.nicko.event.PlayerJoinListener;
import xyz.ineanto.nicko.event.PlayerQuitListener;
import xyz.ineanto.nicko.i18n.Locale;
import xyz.ineanto.nicko.i18n.LocaleFileManager;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.ineanto.nicko.placeholder.NickoExpansion;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.ineanto.nicko.storage.json.JSONStorage;
import xyz.ineanto.nicko.storage.map.MapCache;
import xyz.ineanto.nicko.storage.name.PlayerNameStore;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NickoBukkit extends JavaPlugin {
    private static NickoBukkit plugin;

    private final boolean unitTesting;

    private MojangAPI mojangAPI;
    private PlayerDataStore dataStore;
    private ConfigurationManager configurationManager;
    private Configuration configuration;
    private LocaleFileManager localeFileManager;
    private PlayerNameStore nameStore;
    private Metrics metrics;

    public NickoBukkit() {
        this.unitTesting = false;
    }

    /**
     * Used by MockBukkit
     */
    protected NickoBukkit(Configuration configuration) {
        this.unitTesting = true;
        this.configuration = configuration;
        getLogger().info("Unit Testing Mode enabled.");
    }

    @Override
    public void onEnable() {
        plugin = this;

        configurationManager = new ConfigurationManager(getDataFolder());
        configurationManager.saveDefaultConfig();

        mojangAPI = new MojangAPI();
        dataStore = new PlayerDataStore(mojangAPI, getNickoConfig());
        nameStore = new PlayerNameStore();

        if (!Bukkit.getOnlineMode()) {
            getLogger().warning("Nicko has not been tested using offline mode!");
            getLogger().warning("Issues regarding Nicko being used in offline mode will be ignored for now.");
        }

        if (!MinecraftVersion.WILD_UPDATE.atOrAbove()) {
            getLogger().severe("This version (" + MinecraftVersion.getCurrentVersion().getVersion() + ") is not supported by Nicko!");
            dataStore.getStorage().setError(true);
            Bukkit.getPluginManager().disablePlugin(this);
        }

        getLogger().info("Loading persistence...");
        if (!dataStore.getStorage().getProvider().init()) {
            getLogger().severe("Couldn't connect to distant persistence, falling back on local persistence.");
            final JSONStorage storage = new JSONStorage();
            storage.getProvider().init();
            dataStore.setStorage(storage);
        }

        getLogger().info("Loading cache...");
        if (!dataStore.getCache().getProvider().init()) {
            getLogger().severe("Couldn't connect to distant cache, falling back on local cache.");
            final MapCache cache = new MapCache();
            cache.getProvider().init();
            dataStore.setCache(cache);
        }

        if (!unitTesting) {
            // Migrate configuration (1.0.8-RC1)
            if (configuration.getVersion() == null
                || configuration.getVersion().isEmpty()
                || configuration.getVersionObject().compareTo(Configuration.VERSION) != 0) {
                getLogger().info("Migrating configuration file from older version...");
                try {
                    Files.copy(configurationManager.getFile().toPath(), configurationManager.getBackupFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.delete(configurationManager.getFile().toPath());
                    configurationManager.saveDefaultConfig();
                } catch (IOException e) {
                    getLogger().severe("Failed to migrate your configuration!");
                }
            }

            try {
                Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
                getLogger().warning("Nicko has not been tested against Folia and might not work at all!");
                getLogger().warning("Issues regarding Nicko on Folia will be ignored for now.");
            } catch (ClassNotFoundException ignored) { }

            if (configuration.isCustomLocale()) {
                localeFileManager = new LocaleFileManager();
                if (localeFileManager.dumpFromLocale(Locale.ENGLISH)) {
                    getLogger().info("Successfully loaded custom language file.");
                } else {
                    getLogger().severe("Failed to load custom language file!");
                }
            }

            final PluginCommand command = getCommand("nicko");
            if (command != null) {
                command.setExecutor(new NickoCommand());
            }

            Structure.addGlobalIngredient('#', new SimpleItem(new ItemBuilder(Material.AIR)));
            Structure.addGlobalIngredient('%', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ")));

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                getLogger().info("Enabling PlaceHolderAPI support...");
                new NickoExpansion(this).register();
            }

            getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
            metrics = new Metrics(this, 20483);
        }

        getLogger().info("Nicko has been enabled.");
    }

    @Override
    public void onDisable() {
        if (!getDataStore().getStorage().isError()) {
            nameStore.clearStoredNames();
            Bukkit.getOnlinePlayers().forEach(player -> dataStore.saveData(player));
            if (!dataStore.getStorage().getProvider().close()) {
                getLogger().severe("Failed to close persistence!");
            } else {
                getLogger().info("Persistence closed.");
            }
        }

        if (!unitTesting) metrics.shutdown();
        getLogger().info("Nicko (Bukkit) has been disabled.");
    }

    public static NickoBukkit getInstance() {
        return plugin;
    }

    public Configuration getNickoConfig() {
        try {
            if (configuration == null) {
                configuration = configurationManager.load();
                getLogger().info("Configuration file loaded.");
            }
            return configuration;
        } catch (IOException e) {
            getLogger().severe("Failed to load the configuration file!");
            getLogger().severe("It may be have been generated with an older version of Nicko.");
            getLogger().severe("Delete the configuration and restart the server please :)");
            getLogger().severe("(" + e.getMessage() + ")");
            return null;
        }
    }

    public PlayerDataStore getDataStore() {
        return dataStore;
    }

    public PlayerNameStore getNameStore() {
        return nameStore;
    }

    public MojangAPI getMojangAPI() {
        return mojangAPI;
    }

    public LocaleFileManager getLocaleFileManager() {
        return localeFileManager;
    }
}
