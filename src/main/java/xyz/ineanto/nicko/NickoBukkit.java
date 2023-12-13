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
import xyz.ineanto.nicko.storage.name.PlayerNameStore;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.io.IOException;

public class NickoBukkit extends JavaPlugin {
    private static NickoBukkit plugin;

    private final boolean unitTesting;

    private MojangAPI mojangAPI;
    private PlayerDataStore dataStore;
    private ConfigurationManager configurationManager;
    private Configuration configuration;
    private LocaleFileManager localeFileManager;
    private PlayerNameStore nameStore;

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
            dataStore.getStorage().setError(true);
            getLogger().severe("Failed to open persistence, data will NOT be saved!");
        }

        getLogger().info("Loading cache...");
        if (!dataStore.getCache().getProvider().init()) {
            dataStore.getCache().setError(true);
            getLogger().severe("Failed to open cache, data will NOT be saved!");
        }

        if (!unitTesting) {
            try {
                Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
                getLogger().warning("Nicko has not been tested against Folia and might not work at all!");
                getLogger().warning("Issues regarding Nicko on Folia will be ignored for now.");
            } catch (ClassNotFoundException ignored) { }

            localeFileManager = new LocaleFileManager();
            if (configuration.isCustomLocale()) {
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
            new Metrics(this, 20483);
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

        getLogger().info("Nicko (Bukkit) has been disabled.");
    }

    public static NickoBukkit getInstance() {
        return plugin;
    }

    public Configuration getNickoConfig() {
        try {
            if (configuration == null) { return configuration = configurationManager.load(); }
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
