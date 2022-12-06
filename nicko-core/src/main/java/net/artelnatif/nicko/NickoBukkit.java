package net.artelnatif.nicko;

import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.SimpleItem;
import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.command.NickoCommand;
import net.artelnatif.nicko.command.NickoTabCompleter;
import net.artelnatif.nicko.config.NickoConfiguration;
import net.artelnatif.nicko.event.PlayerJoinListener;
import net.artelnatif.nicko.event.PlayerQuitListener;
import net.artelnatif.nicko.gui.items.main.ExitDoorItem;
import net.artelnatif.nicko.i18n.I18N;
import net.artelnatif.nicko.i18n.LocaleManager;
import net.artelnatif.nicko.impl.Internals;
import net.artelnatif.nicko.impl.InternalsProvider;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.pluginchannel.PluginMessageHandler;
import net.artelnatif.nicko.storage.PlayerDataStore;
import net.artelnatif.nicko.utils.ServerUtils;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class NickoBukkit extends JavaPlugin {
    private static NickoBukkit plugin;

    private NickoConfiguration nickoConfiguration;
    private MojangAPI mojangAPI;
    private PlayerDataStore dataStore;
    private I18N i18N;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("Loading internals...");
        if (getInternals() == null) {
            getLogger().log(Level.SEVERE, "Nicko could not find a valid implementation for this server version. Is your server supported?");
            dataStore.getStorage().setError(true);
            getServer().getPluginManager().disablePlugin(this);
        }

        if (getServer().getPluginManager().isPluginEnabled(this)) {
            mojangAPI = new MojangAPI();

            getLogger().info("Loading configuration...");
            saveDefaultConfig();
            nickoConfiguration = new NickoConfiguration(this);

            LocaleManager.setDefaultLocale(this);

            final PluginCommand command = getCommand("nicko");
            if (command != null) {
                command.setExecutor(new NickoCommand());
                command.setTabCompleter(new NickoTabCompleter());
            }

            getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

            Structure.addGlobalIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)));
            Structure.addGlobalIngredient('%', new SimpleItem(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE)));
            Structure.addGlobalIngredient('E', new ExitDoorItem());

            getLogger().info("Loading persistence...");
            dataStore = new PlayerDataStore(this);

            if (!dataStore.getStorage().getProvider().init()) {
                dataStore.getStorage().setError(true);
                getLogger().warning("Failed to open persistence, data will NOT be saved!");
            }

            ServerUtils.checkSpigotBungeeCordHook();
            if (nickoConfiguration.isBungeecordEnabled()) {
                if (ServerUtils.checkBungeeCordHook()) {
                    getLogger().info("Enabling BungeeCord support...");
                    getServer().getMessenger().registerIncomingPluginChannel(this, NickoBungee.NICKO_PLUGIN_CHANNEL_UPDATE, new PluginMessageHandler());
                }
            }

            getLogger().info("Nicko (Bukkit) has been enabled.");
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

        if (nickoConfiguration.isBungeecordEnabled()) {
            getServer().getMessenger().unregisterIncomingPluginChannel(this);
            getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        }

        getLogger().info("Nicko (Bukkit) has been disabled.");
    }

    public static NickoBukkit getInstance() {
        return plugin;
    }

    public MojangAPI getMojangAPI() {
        return mojangAPI;
    }

    public NickoConfiguration getNickoConfig() { return nickoConfiguration; }

    public PlayerDataStore getDataStore() { return dataStore; }

    public Internals getInternals() {
        return InternalsProvider.getInternals();
    }
}
