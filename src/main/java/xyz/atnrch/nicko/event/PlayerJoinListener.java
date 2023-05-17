package xyz.atnrch.nicko.event;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.disguise.AppearanceManager;
import xyz.atnrch.nicko.disguise.ActionResult;
import xyz.atnrch.nicko.disguise.NickoProfile;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.storage.PlayerDataStore;
import xyz.atnrch.nicko.storage.name.PlayerNameStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final NickoBukkit instance = NickoBukkit.getInstance();

        final PlayerDataStore dataStore = instance.getDataStore();
        final PlayerNameStore nameStore = instance.getNameStore();
        nameStore.storeName(player);

        // TODO: 2/20/23 BungeeCord transfer

        dataStore.performProfileUpdate(player.getUniqueId(), NickoProfile.EMPTY_PROFILE);
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            final AppearanceManager appearanceManager = AppearanceManager.get(player);
            if (appearanceManager.hasData()) {
                final ActionResult<Void> actionResult = appearanceManager.updatePlayer(appearanceManager.needsASkinChange());
                if (!actionResult.isError()) {
                    player.sendMessage(I18N.translate(player, I18NDict.Event.PreviousSkin.SUCCESS));
                } else {
                    player.sendMessage(I18N.translate(player, I18NDict.Event.PreviousSkin.FAIL, I18N.translateWithoutPrefix(player, actionResult.getErrorMessage())));
                }
            }
        }, 20L);
    }
}
