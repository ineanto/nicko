package net.artelnatif.nicko.event;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.AppearanceManager;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.i18n.I18N;
import net.artelnatif.nicko.i18n.I18NDict;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        NickoBukkit.getInstance().getDataStore().storeName(player);
        Bukkit.getScheduler().runTaskLater(NickoBukkit.getInstance(), () -> {
            final AppearanceManager appearanceManager = AppearanceManager.get(player);

            // TODO: 12/5/22 Update from BungeeCord

            if (appearanceManager.hasData()) {
                final ActionResult actionResult = appearanceManager.updatePlayer(appearanceManager.needsASkinChange());
                if (!actionResult.isError()) {
                    player.sendMessage(I18N.translate(player, I18NDict.Event.PreviousSkin.SUCCESS));
                } else {
                    player.sendMessage(I18N.translate(player, I18NDict.Event.PreviousSkin.FAIL, I18N.translate(player, actionResult.getErrorMessage())));
                }
            }
        }, 20L);
    }
}
