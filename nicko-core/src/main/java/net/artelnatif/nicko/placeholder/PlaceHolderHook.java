package net.artelnatif.nicko.placeholder;

import net.artelnatif.nicko.NickoBukkit;
import org.bukkit.Bukkit;

public class PlaceHolderHook {
    private final NickoBukkit instance;

    public PlaceHolderHook(NickoBukkit instance) {
        this.instance = instance;
    }

    public void hook() {
        instance.getLogger().info("Checking support for PlaceHolderAPI...");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new NickoExpension(instance).register();
            instance.getLogger().info("Hooked into PlaceHolderAPI!");
        }
    }
}
