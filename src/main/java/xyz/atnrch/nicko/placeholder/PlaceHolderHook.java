package xyz.atnrch.nicko.placeholder;

import xyz.atnrch.nicko.NickoBukkit;
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
