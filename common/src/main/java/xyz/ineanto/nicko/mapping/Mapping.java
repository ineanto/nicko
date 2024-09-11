package xyz.ineanto.nicko.mapping;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.ineanto.nicko.storage.name.PlayerNameStore;

import java.util.Set;

public abstract class Mapping {
    private final Nicko instance = Nicko.getInstance();
    private final PlayerDataStore dataStore = instance.getDataStore();
    private final PlayerNameStore nameStore = instance.getNameStore();

    public abstract void respawn(Player player);

    public abstract Set<String> supportedVersions();
}
