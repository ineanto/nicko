package net.artelnatif.nicko.bukkit;

import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.SimpleItem;
import net.artelnatif.nicko.Nicko;
import net.artelnatif.nicko.bukkit.command.NickoCommand;
import net.artelnatif.nicko.bukkit.event.PlayerJoinListener;
import net.artelnatif.nicko.bukkit.event.PlayerQuitListener;
import net.artelnatif.nicko.bukkit.gui.items.main.ExitGUI;
import net.artelnatif.nicko.bukkit.i18n.Locale;
import net.artelnatif.nicko.bukkit.i18n.LocaleFileManager;
import net.artelnatif.nicko.bukkit.placeholder.PlaceHolderHook;
import net.artelnatif.nicko.bukkit.pluginchannel.PluginMessageHandler;
import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.impl.Internals;
import net.artelnatif.nicko.impl.InternalsProvider;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class NickoBukkit extends JavaPlugin {
    private static NickoBukkit plugin;

    private final Nicko nicko = new Nicko();
    private final boolean unitTesting;

    private LocaleFileManager localeFileManager;

    public NickoBukkit() { this.unitTesting = false; }

    /**
     * Used by MockBukkit
     */
    protected NickoBukkit(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file, Configuration config) {
        super(loader, description, dataFolder, file);
        unitTesting = true;
        nicko.setConfig(config);
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

    public void onUnitTestingStartup() {
        nicko.initBukkit(this);

        if (!nicko.getDataStore().getStorage().getProvider().init()) {
            nicko.getDataStore().getStorage().setError(true);
            getLogger().severe("Failed to open persistence, data will NOT be saved!");
        }
    }

    public void onPluginStartup() {
        nicko.initBukkit(this);

        getLogger().info("Loading internals...");
        if (getInternals() == null) {
            getLogger().severe("Nicko could not find a valid implementation for this server version. Is your server supported?");
            nicko.getDataStore().getStorage().setError(true);
            getServer().getPluginManager().disablePlugin(this);
        }

        if (getServer().getPluginManager().isPluginEnabled(this) && !nicko.getDataStore().getStorage().isError()) {
            getLogger().info("Loading persistence...");
            if (!nicko.getDataStore().getStorage().getProvider().init()) {
                nicko.getDataStore().getStorage().setError(true);
                getLogger().severe("Failed to open persistence, data will NOT be saved!");
            }

            localeFileManager = new LocaleFileManager();
            if (nicko.getConfig().isCustomLocale()) {
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

            final BungeeCordSupport support = new BungeeCordSupport(this);
            support.warnNickoNotHookedToBungeeCord();
            if (nicko.getConfig().isBungeecord()) {
                if (support.stopIfBungeeCordIsNotEnabled()) {
                    getLogger().info("Enabling BungeeCord support...");
                    getServer().getMessenger().registerIncomingPluginChannel(this, NickoBungee.PROXY_UPDATE, new PluginMessageHandler());
                }
            }

            getLogger().info("Nicko (Bukkit) has been enabled.");
        }
    }

    @Override
    public void onDisable() {
        if (!nicko.getDataStore().getStorage().isError()) {
            getLogger().info("Closing persistence...");
            nicko.getDataStore().removeAllNames();
            Bukkit.getOnlinePlayers().forEach(player -> nicko.getDataStore().saveData(player));
            if (!nicko.getDataStore().getStorage().getProvider().close()) {
                getLogger().severe("Failed to close persistence!");
            }
        }

        if (nicko.getConfig().isBungeecord()) {
            getServer().getMessenger().unregisterIncomingPluginChannel(this);
            getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        }

        getLogger().info("Nicko (Bukkit) has been disabled.");
    }

    public static NickoBukkit getInstance() {
        return plugin;
    }

    public Nicko getNicko() {
        return nicko;
    }

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
