package net.artelnatif.nicko.placeholder;

import net.artelnatif.nicko.NickoBukkit;
import org.bukkit.Bukkit;

public class PlaceHolderHook {
    private final NickoBukkit instance;

    public PlaceHolderHook(NickoBukkit instance) {
        this.instance = instance;
    }

    public void hook() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            instance.getLogger().info("Enabling PlaceHolderAPI support...");
            new NickoExpension(instance).register();
        }
    }
}
