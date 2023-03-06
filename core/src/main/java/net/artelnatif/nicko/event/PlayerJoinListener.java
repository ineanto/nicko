package net.artelnatif.nicko.event;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.appearance.AppearanceManager;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.I18N;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.storage.PlayerDataStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
        dataStore.storeName(player);

        // TODO: 2/20/23 BungeeCord transfer

        dataStore.performProfileUpdate(player.getUniqueId(), NickoProfile.EMPTY_PROFILE);
        Bukkit.getScheduler().runTaskLater(NickoBukkit.getInstance(), () -> {
            final AppearanceManager appearanceManager = AppearanceManager.get(player);

            if (appearanceManager.hasData()) {
                final ActionResult<Void> actionResult = appearanceManager.updatePlayer(appearanceManager.needsASkinChange());
                if (!actionResult.isError()) {
                    player.sendMessage(I18N.translate(player, I18NDict.Event.PreviousSkin.SUCCESS));
                } else {
                    player.sendMessage(I18N.translate(player, I18NDict.Event.PreviousSkin.FAIL, I18N.translate(player, actionResult.getErrorMessage())));
                }
            }
        }, 20L);
    }
}
