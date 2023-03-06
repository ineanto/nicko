package net.artelnatif.nicko;

import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.SimpleItem;
import net.artelnatif.nicko.command.NickoCommand;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.config.ConfigurationManager;
import net.artelnatif.nicko.event.PlayerJoinListener;
import net.artelnatif.nicko.event.PlayerQuitListener;
import net.artelnatif.nicko.gui.items.main.ExitGUI;
import net.artelnatif.nicko.i18n.Locale;
import net.artelnatif.nicko.i18n.LocaleFileManager;
import net.artelnatif.nicko.impl.Internals;
import net.artelnatif.nicko.impl.InternalsProvider;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.placeholder.PlaceHolderHook;
import net.artelnatif.nicko.storage.PlayerDataStore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.IOException;

public class NickoBukkit extends JavaPlugin {
    private static NickoBukkit plugin;

    private ConfigurationManager configurationManager;
    private File dataFolder;
    private MojangAPI mojangAPI;
    private Configuration configuration;
    private PlayerDataStore dataStore;
    private final boolean unitTesting;

    private LocaleFileManager localeFileManager;

    public NickoBukkit() { this.unitTesting = false; }

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

        if (!getDataStore().getStorage().isError()) {
            getLogger().info("Loading persistence...");
            if (!getDataStore().getStorage().getProvider().init()) {
                getDataStore().getStorage().setError(true);
                getLogger().severe("Failed to open persistence, data will NOT be saved!");
            }
        }

        if (!unitTesting) {
            getLogger().info("Loading internals...");
            if (getInternals() == null) {
                getLogger().severe("Nicko could not find a valid implementation for this server version. Is your server supported?");
                getDataStore().getStorage().setError(true);
                getServer().getPluginManager().disablePlugin(this);
            }


            localeFileManager = new LocaleFileManager();
            if (getNickoConfig().isCustomLocale()) {
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
            getDataStore().removeAllNames();
            Bukkit.getOnlinePlayers().forEach(player -> getDataStore().saveData(player));
            if (!getDataStore().getStorage().getProvider().close()) {
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
            getLogger().severe("Failed to load configuration file: " + e.getMessage());
            return null;
        }
    }

    public PlayerDataStore getDataStore() {
        return dataStore;
    }

    public MojangAPI getMojangAPI() {
        return mojangAPI;
    }

    public LocaleFileManager getLocaleFileManager() {
        return localeFileManager;
    }

    public Internals getInternals() {
        return InternalsProvider.getInternals();
    }
}