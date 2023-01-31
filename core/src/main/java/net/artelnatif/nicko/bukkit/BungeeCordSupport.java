package net.artelnatif.nicko.bukkit;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;

public class BungeeCordSupport {
    private final NickoBukkit instance;

    public BungeeCordSupport(NickoBukkit instance) {
        this.instance = instance;
    }

    public void warnNickoNotHookedToBungeeCord() {
        final Server server = instance.getServer();
        final YamlConfiguration config = server.spigot().getConfig();
        if (config.getConfigurationSection("settings").getBoolean("bungeecord") && !instance.getNicko().getConfig().isBungeecord()) {
            instance.getLogger().warning("Hummm. Your server is hooked to BungeeCord, but it seems");
            instance.getLogger().warning("that BungeeCord support is not enabled inside Nicko.");
            instance.getLogger().warning("If this is intentional, you can safely ignore this message.");
            instance.getLogger().warning("Otherwise, you can enable BungeeCord support inside Nicko's configuration file.");
        }
    }

    public boolean stopIfBungeeCordIsNotEnabled() {
        final Server server = instance.getServer();
        final YamlConfiguration config = server.spigot().getConfig();
        if (!config.getConfigurationSection("settings").getBoolean("bungeecord") && instance.getNicko().getConfig().isBungeecord()) {
            instance.getLogger().severe("Hummm. You have enabled BungeeCord support inside Nicko,");
            instance.getLogger().severe("but it seems that your server is not hooked to your BungeeCord instance.");
            instance.getLogger().severe("Please enable BungeeCord support inside your spigot.yml as well.");
            instance.getLogger().severe("The plugin will not continue.");
            instance.getServer().getPluginManager().disablePlugin(instance);
            return false;
        }
        return true;
    }
}
