package xyz.atnrch.nicko.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.ActionResult;
import xyz.atnrch.nicko.appearance.AppearanceManager;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.storage.name.PlayerNameStore;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final NickoBukkit instance = NickoBukkit.getInstance();
        final I18N i18n = new I18N(player);
        final PlayerNameStore nameStore = instance.getNameStore();

        // TODO: 2/20/23 Fetch data from BungeeCord
        nameStore.storeName(player);
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            final AppearanceManager appearanceManager = AppearanceManager.get(player);
            if (appearanceManager.hasData()) {
                final ActionResult actionResult = appearanceManager.updatePlayer(appearanceManager.needsASkinChange(), false);
                if (!actionResult.isError()) {
                    player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Restore.OK));
                } else {
                    player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Restore.ERROR, i18n.translatePrefixless(actionResult.getErrorKey())));
                }
            }
        }, 20L);
        System.out.println("i18n.getPlayerLocale().getCode() = " + i18n.getPlayerLocale().getCode());
    }
}
