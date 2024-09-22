package xyz.ineanto.nicko.mapping;

import org.bukkit.entity.Player;

import java.util.Set;

public abstract class Mapping {
    public abstract void respawn(Player player);

    public abstract Set<String> supportedVersions();
}
