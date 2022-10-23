package net.artelnatif.nicko.event;

import net.artelnatif.nicko.NickoBukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        NickoBukkit.getInstance().getDataStore().saveData(player);
    }
}
