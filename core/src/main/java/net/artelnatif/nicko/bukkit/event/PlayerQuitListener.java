package net.artelnatif.nicko.bukkit.event;

import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final ActionResult<Void> result = NickoBukkit.getInstance().getNicko().getDataStore().saveData(player);
        if (result.isError()) {
            NickoBukkit.getInstance().getLogger().warning("Failed to save data for " + player.getName());
        }
    }
}
