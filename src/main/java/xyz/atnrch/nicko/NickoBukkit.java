package xyz.atnrch.nicko;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import xyz.atnrch.nicko.command.NickoCommand;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.ConfigurationManager;
import xyz.atnrch.nicko.event.PlayerJoinListener;
import xyz.atnrch.nicko.event.PlayerQuitListener;
import xyz.atnrch.nicko.gui.items.common.OptionUnavailable;
import xyz.atnrch.nicko.gui.items.main.ExitGUI;
import xyz.atnrch.nicko.i18n.Locale;
import xyz.atnrch.nicko.i18n.LocaleFileManager;
import xyz.atnrch.nicko.mojang.MojangAPI;
import xyz.atnrch.nicko.placeholder.PlaceHolderHook;
import xyz.atnrch.nicko.storage.PlayerDataStore;
import xyz.atnrch.nicko.storage.name.PlayerNameStore;
import xyz.xenondevs.invui.gui.structure.Structure;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.io.File;
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
    private ProtocolManager protocolManager;

    public NickoBukkit() { this.unitTesting = false; }

    /**
     * Used by MockBukkit
     */
    protected NickoBukkit(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        this(loader, description, dataFolder, file, null);
    }

    /**
     * Used by MockBukkit
     */
    protected NickoBukkit(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file, Configuration configuration) {
        super(loader, description, dataFolder, file);
        unitTesting = true;
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
            protocolManager = ProtocolLibrary.getProtocolManager();
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

            Structure.addGlobalIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ")));
            Structure.addGlobalIngredient('%', new SimpleItem(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayName(" ")));
            Structure.addGlobalIngredient('U', new OptionUnavailable());
            Structure.addGlobalIngredient('E', new ExitGUI());

            new PlaceHolderHook(this).hook();

            getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

            getLogger().info("Nicko (Bukkit) has been enabled.");
        }
    }

    @Override
    public void onDisable() {
        if (!getDataStore().getStorage().isError()) {
            getLogger().info("Closing persistence...");
            nameStore.clearStoredNames();
            Bukkit.getOnlinePlayers().forEach(player -> dataStore.saveData(player));
            if (!dataStore.getStorage().getProvider().close()) {
                getLogger().severe("Failed to close persistence!");
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

    public ProtocolManager getProtocolManager() { return protocolManager; }
}
