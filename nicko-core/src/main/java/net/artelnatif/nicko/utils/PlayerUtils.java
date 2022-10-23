package net.artelnatif.nicko.utils;

import org.bukkit.entity.Player;

public class PlayerUtils {
    public static boolean isPlayerOffline(Player player) {
        return player == null || !player.isOnline();
    }
}
