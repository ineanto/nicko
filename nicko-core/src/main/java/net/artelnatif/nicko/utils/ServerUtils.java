package net.artelnatif.nicko.utils;

import net.artelnatif.nicko.NickoBukkit;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;

public class ServerUtils {
    public static boolean checkBungeeCordHook() {
        final NickoBukkit instance = NickoBukkit.getInstance();
        final Server server = instance.getServer();
        final YamlConfiguration config = server.spigot().getConfig();
        if (!config.getConfigurationSection("settings").getBoolean("bungeecord")) {
            instance.getLogger().severe("Hummm. You have enabled BungeeCord support inside Nicko,");
            instance.getLogger().severe("but it seems that your server is not hooked to your BungeeCord instance.");
            instance.getLogger().severe("If the server is already hooked to BungeeCord, please enable it into your spigot.yml aswell.");
            instance.getLogger().severe("The plugin will not continue.");
            instance.getServer().getPluginManager().disablePlugin(instance);
            return false;
        }
        return true;
    }
}
