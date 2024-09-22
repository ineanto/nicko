package xyz.ineanto.nicko;

import com.comphenix.protocol.utility.MinecraftVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.ineanto.nicko.appearance.random.RandomNameFetcher;
import xyz.ineanto.nicko.command.NickoCommand;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.ConfigurationManager;
import xyz.ineanto.nicko.event.PlayerJoinListener;
import xyz.ineanto.nicko.event.PlayerQuitListener;
import xyz.ineanto.nicko.language.CustomLanguage;
import xyz.ineanto.nicko.language.Language;
import xyz.ineanto.nicko.mapping.MappingManager;
import xyz.ineanto.nicko.migration.ConfigurationMigrator;
import xyz.ineanto.nicko.migration.CustomLocaleMigrator;
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

public class Nicko extends JavaPlugin {
    private static Nicko plugin;

    private final boolean unitTesting;

    private MojangAPI mojangAPI;
    private PlayerDataStore dataStore;
    private ConfigurationManager configurationManager;
    private Configuration configuration;
    private CustomLanguage customLanguage;
    private PlayerNameStore nameStore;
    private RandomNameFetcher nameFetcher;
    private MappingManager mappingManager;
    private Metrics metrics;

    public Nicko() {
        this.unitTesting = false;
    }

    /**
     * Used by MockBukkit
     */
    protected Nicko(Configuration configuration) {
        this.unitTesting = true;
        this.configuration = configuration;
        getLogger().info("Unit Testing Mode enabled.");
    }

    @Override
    public void onEnable() {
        plugin = this;

        configurationManager = new ConfigurationManager(getDataFolder());
        configurationManager.saveDefaultConfig();

        dataStore = new PlayerDataStore(mojangAPI, getNickoConfig());
        mappingManager = new MappingManager();

        if (!MinecraftVersion.TRAILS_AND_TAILS.atOrAbove() || mappingManager.getMappingForServer().isEmpty()) {
            getLogger().severe("This version (" + MinecraftVersion.getCurrentVersion().getVersion() + ") is not supported by Nicko!");
            getLogger().severe("As of version 1.0.7, Nicko only supports the latest two majors Minecraft versions. (Currently 1.20.X-1.21.X)");
            dataStore.getStorage().setError(true);
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (!Bukkit.getOnlineMode()) {
            getLogger().warning("Nicko has not been tested using offline mode!");
            getLogger().warning("Issues regarding Nicko being used in offline mode will be ignored for now.");
        }

        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
            getLogger().warning("Nicko has not been tested against Folia and might not work at all!");
            getLogger().warning("Issues regarding Nicko on Folia will be ignored for now.");
        } catch (ClassNotFoundException ignored) { }

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
            nameStore = new PlayerNameStore();
            mojangAPI = new MojangAPI();
            nameFetcher = new RandomNameFetcher(this);

            new ConfigurationMigrator(this).migrate();

            if (configuration.isCustomLocale()) {
                try {
                    CustomLanguage.dumpIntoFile(Language.ENGLISH);
                    customLanguage = new CustomLanguage();
                    new CustomLocaleMigrator(this, customLanguage).migrate();
                    getLogger().info("Successfully loaded the custom locale.");
                } catch (IOException e) {
                    getLogger().severe("Failed to load the custom locale!");
                }
            }

            final PluginCommand command = getCommand("xyz/ineanto/nicko");
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
            Bukkit.getOnlinePlayers().forEach(player -> dataStore.saveData(player));
            if (!dataStore.getStorage().getProvider().close()) {
                getLogger().severe("Failed to close persistence!");
            } else {
                getLogger().info("Persistence closed.");
            }
        }

        if (!unitTesting) {
            nameStore.clearStoredNames();
            metrics.shutdown();
        }
        getLogger().info("Nicko (Bukkit) has been disabled.");
    }

    public static Nicko getInstance() {
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

    public RandomNameFetcher getNameFetcher() {
        return nameFetcher;
    }

    public PlayerDataStore getDataStore() {
        return dataStore;
    }

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public PlayerNameStore getNameStore() {
        return nameStore;
    }

    public MojangAPI getMojangAPI() {
        return mojangAPI;
    }

    public CustomLanguage getCustomLocale() {
        return customLanguage;
    }

    public MappingManager getMappingManager() {
        return mappingManager;
    }
}
