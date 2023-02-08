package net.artelnatif.nicko.bungee;

import net.artelnatif.nicko.Nicko;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class NickoBungee extends Plugin {
    private final HashMap<UUID, NickoProfile> profiles = new HashMap<>();
    private final Nicko nicko = new Nicko();

    private static NickoBungee plugin;

    @Override
    public void onEnable() {
        plugin = this;
        nicko.initBungeecord(this);

        getLogger().info("Loading persistence...");
        if (!nicko.getDataStore().getStorage().isError()) {
            if (!nicko.getDataStore().getStorage().getProvider().init()) {
                getLogger().severe("Failed to load persistence!");
                getLogger().severe("Nicko can't enable BungeeCord support without SQL storage.");
                getLogger().severe("The plugin will not continue.");
                nicko.getDataStore().getStorage().setError(true);
                onDisable();
                return;
            }

            getLogger().info("Nicko (Bungee) has been enabled.");
        }
    }

    @Override
    public void onDisable() {
        if (!nicko.getDataStore().getStorage().isError()) {
            getLogger().info("Nicko (Bungee) has been disabled.");
        }
    }

    public Nicko getNicko() {
        return nicko;
    }

    public static NickoBungee getInstance() {
        return plugin;
    }
}
