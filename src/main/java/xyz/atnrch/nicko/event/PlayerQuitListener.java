package xyz.atnrch.nicko.event;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.disguise.ActionResult;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final ActionResult<Void> result = NickoBukkit.getInstance().getDataStore().saveData(player);
        if (result.isError()) {
            NickoBukkit.getInstance().getLogger().warning("Failed to save data for " + player.getName());
        }
    }
}
