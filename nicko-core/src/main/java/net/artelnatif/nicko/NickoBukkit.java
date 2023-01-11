package net.artelnatif.nicko;

import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.SimpleItem;
import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.command.NickoCommand;
import net.artelnatif.nicko.config.NickoConfiguration;
import net.artelnatif.nicko.event.PlayerJoinListener;
import net.artelnatif.nicko.event.PlayerQuitListener;
import net.artelnatif.nicko.gui.items.main.ExitDoorItem;
import net.artelnatif.nicko.i18n.Locale;
import net.artelnatif.nicko.i18n.LocaleFileManager;
import net.artelnatif.nicko.impl.Internals;
import net.artelnatif.nicko.impl.InternalsProvider;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.placeholder.PlaceHolderHook;
import net.artelnatif.nicko.pluginchannel.PluginMessageHandler;
import net.artelnatif.nicko.storage.PlayerDataStore;
import net.artelnatif.nicko.utils.ServerUtils;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.logging.Level;

public class NickoBukkit extends JavaPlugin {
    private static NickoBukkit plugin;

    private final boolean unitTesting;

    private NickoConfiguration config;
    private MojangAPI mojangAPI;
    private PlayerDataStore dataStore;
    private LocaleFileManager localeFileManager;

    public NickoBukkit() { this.unitTesting = false; }

    /**
     * Used by MockBukkit
     */
    protected NickoBukkit(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file, NickoConfiguration config) {
        super(loader, description, dataFolder, file);
        unitTesting = true;
        this.config = config;
        getLogger().info("Unit Testing Mode enabled.");
    }

    @Override
    public void onEnable() {
        plugin = this;

        if (isUnitTesting()) {
            onUnitTestingStartup();
        } else {
            onPluginStartup();
        }
    }

    @Override
    public void onDisable() {
        if (!dataStore.getStorage().isError()) {
            getLogger().info("Closing persistence...");
            dataStore.removeAllNames();
            if (!dataStore.getStorage().getProvider().close()) {
                getLogger().warning("Failed to close persistence!");
            }
            dataStore.getStorage().setError(false);
        }

        if (config.isBungeecordSupport()) {
            getServer().getMessenger().unregisterIncomingPluginChannel(this);
            getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        }

        getLogger().info("Nicko (Bukkit) has been disabled.");
    }

    public void onUnitTestingStartup() {
        getLogger().info("Loading persistence...");
        dataStore = new PlayerDataStore(this);

        if (!dataStore.getStorage().getProvider().init()) {
            dataStore.getStorage().setError(true);
            getLogger().warning("Failed to open persistence, data will NOT be saved!");
        }
    }

    public void onPluginStartup() {
        getLogger().info("Loading internals...");
        if (getInternals() == null) {
            getLogger().log(Level.SEVERE, "Nicko could not find a valid implementation for this server version. Is your server supported?");
            dataStore.getStorage().setError(true);
            getServer().getPluginManager().disablePlugin(this);
        }

        if (getServer().getPluginManager().isPluginEnabled(this)) {
            mojangAPI = new MojangAPI(this);

            getLogger().info("Loading configuration...");
            saveDefaultConfig();
            config = new NickoConfiguration(this);

            localeFileManager = new LocaleFileManager();
            if (config.isCustomLocale()) {
                if (localeFileManager.dumpFromLocale(Locale.ENGLISH)) {
                    if (localeFileManager.loadValues()) {
                        getLogger().info("Successfully loaded custom language file.");
                    }
                } else {
                    getLogger().warning("Failed to load custom language file!");
                }
            }

            final PluginCommand command = getCommand("nicko");
            if (command != null) {
                command.setExecutor(new NickoCommand());
            }

            Structure.addGlobalIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)));
            Structure.addGlobalIngredient('%', new SimpleItem(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE)));
            Structure.addGlobalIngredient('E', new ExitDoorItem());

            getLogger().info("Loading persistence...");
            dataStore = new PlayerDataStore(this);

            if (!dataStore.getStorage().getProvider().init()) {
                dataStore.getStorage().setError(true);
                getLogger().warning("Failed to open persistence, data will NOT be saved!");
            }

            new PlaceHolderHook(this).hook();

            getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

            final ServerUtils serverUtils = new ServerUtils(this);
            serverUtils.checkSpigotBungeeCordHook();
            if (config.isBungeecordSupport()) {
                if (serverUtils.checkBungeeCordHook()) {
                    getLogger().info("Enabling BungeeCord support...");
                    getServer().getMessenger().registerIncomingPluginChannel(this, NickoBungee.NICKO_PLUGIN_CHANNEL_UPDATE, new PluginMessageHandler());
                }
            }

            getLogger().info("Nicko (Bukkit) has been enabled.");
        }
    }

    public static NickoBukkit getInstance() {
        return plugin;
    }

    public MojangAPI getMojangAPI() {
        return mojangAPI;
    }

    public NickoConfiguration getNickoConfig() { return config; }

    public PlayerDataStore getDataStore() { return dataStore; }

    public LocaleFileManager getLocaleFileManager() {
        return localeFileManager;
    }

    public boolean isUnitTesting() {
        return unitTesting;
    }

    public Internals getInternals() {
        return InternalsProvider.getInternals();
    }
}
