package net.artelnatif.nicko;

import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.command.NickoCommand;
import net.artelnatif.nicko.command.NickoTabCompleter;
import net.artelnatif.nicko.config.NickoConfiguration;
import net.artelnatif.nicko.event.PlayerJoinListener;
import net.artelnatif.nicko.event.PlayerQuitListener;
import net.artelnatif.nicko.impl.Internals;
import net.artelnatif.nicko.impl.InternalsProvider;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.pluginchannel.UpdateMessageHandler;
import net.artelnatif.nicko.storage.PlayerDataStore;
import net.artelnatif.nicko.utils.ServerUtils;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class NickoBukkit extends JavaPlugin {
    private static NickoBukkit plugin;

    private NickoConfiguration nickoConfiguration;
    private MojangAPI mojangAPI;
    private PlayerDataStore dataStore;

    @Override
    public void onEnable() {
        plugin = this;

        final PluginCommand command = getCommand("nicko");
        if (command != null) {
            command.setExecutor(new NickoCommand());
            command.setTabCompleter(new NickoTabCompleter());
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);

        mojangAPI = new MojangAPI();
        nickoConfiguration = new NickoConfiguration(this);

        getLogger().info("Loading configuration...");
        saveDefaultConfig();

        getLogger().info("Loading persistence...");
        dataStore = new PlayerDataStore(this);
        if (!dataStore.getStorage().getProvider().init()) {
            dataStore.getStorage().setError(true);
            getLogger().warning("Failed to open persistence, data will NOT be saved!");
        }

        if (nickoConfiguration.isBungeecordEnabled()) {
            getLogger().info("Enabling Bungeecord support...");
            if (ServerUtils.checkBungeeCordHook()) {
                getServer().getMessenger().registerIncomingPluginChannel(this, NickoBungee.NICKO_PLUGIN_CHANNEL_UPDATE, new UpdateMessageHandler());
            }
        }

        getLogger().info("Nicko (Bukkit) has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Closing persistence...");
        if (!dataStore.getStorage().getProvider().close()) {
            getLogger().warning("Failed to close persistence!");
        }
        if (dataStore.getStorage().isError()) {
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
