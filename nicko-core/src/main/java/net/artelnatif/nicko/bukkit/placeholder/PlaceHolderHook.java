package net.artelnatif.nicko.bukkit.placeholder;

import net.artelnatif.nicko.bukkit.NickoBukkit;
import org.bukkit.Bukkit;

public class PlaceHolderHook {
    private final NickoBukkit instance;

    public PlaceHolderHook(NickoBukkit instance) {
        this.instance = instance;
    }

    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            instance.getLogger().info("Enabling PlaceHolderAPI support...");
            new NickoExpansion(instance).register();
        }
    }
}
