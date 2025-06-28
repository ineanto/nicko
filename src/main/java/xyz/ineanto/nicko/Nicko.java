package xyz.ineanto.nicko;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.ineanto.nicko.appearance.random.RandomNameFetcher;
import xyz.ineanto.nicko.command.NickoCommand;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.ConfigurationManager;
import xyz.ineanto.nicko.event.PlayerJoinListener;
import xyz.ineanto.nicko.event.PlayerQuitListener;
import xyz.ineanto.nicko.language.CustomLanguage;
import xyz.ineanto.nicko.language.Language;
import xyz.ineanto.nicko.migration.ConfigurationMigrator;
import xyz.ineanto.nicko.migration.CustomLocaleMigrator;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.ineanto.nicko.placeholder.NickoExpansion;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.ineanto.nicko.storage.json.JSONStorage;
import xyz.ineanto.nicko.storage.map.MapCache;
import xyz.ineanto.nicko.storage.name.PlayerNameStore;
import xyz.xenondevs.invui.InvUI;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.io.IOException;

public class Nicko extends JavaPlugin {
    private static Nicko plugin;

    private MojangAPI mojangAPI;
    private PlayerDataStore dataStore;
    private ConfigurationManager configurationManager;
    private Configuration configuration;
    private CustomLanguage customLanguage;
    private PlayerNameStore nameStore;
    private RandomNameFetcher nameFetcher;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        plugin = this;

        PacketEvents.getAPI().init();
        PacketEvents.getAPI().getSettings().checkForUpdates(false).kickOnPacketException(true);

        configurationManager = new ConfigurationManager(getDataFolder());
        configurationManager.saveDefaultConfig();

        dataStore = new PlayerDataStore(mojangAPI, getNickoConfig());

        if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_20)) {
            getLogger().severe("This version (" + PacketEvents.getAPI().getServerManager().getVersion() + ") is not officially supported by Nicko!");
            getLogger().severe("As of version 1.3.0, Nicko only supports the two latest major Minecraft versions. (Currently 1.20 to 1.21.5)");
            getLogger().severe("Do NOT complain about it not working, you've been warned!");
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

        nameStore = new PlayerNameStore();
        mojangAPI = new MojangAPI();
        nameFetcher = new RandomNameFetcher(this);

        new ConfigurationMigrator(this).migrate();
        InvUI.getInstance().setPlugin(this);

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

        registerCommand("nicko", new NickoCommand());

        Structure.addGlobalIngredient('#', new SimpleItem(new ItemBuilder(Material.AIR)));
        Structure.addGlobalIngredient('%', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ")));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("Enabling PlaceHolderAPI support...");
            new NickoExpansion(this).register();
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

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

        nameStore.clearStoredNames();
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
}
