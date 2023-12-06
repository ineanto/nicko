package xyz.ineanto.nicko.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.ineanto.nicko.storage.name.PlayerNameStore;

import java.util.Optional;

public class PlayerJoinListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final NickoBukkit instance = NickoBukkit.getInstance();
        final I18N i18n = new I18N(player);
        final PlayerNameStore nameStore = instance.getNameStore();
        final PlayerDataStore dataStore = instance.getDataStore();

        nameStore.storeName(player);
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            final Optional<NickoProfile> optionalProfile = dataStore.getData(player.getUniqueId());

            optionalProfile.ifPresent(profile -> {
                if (profile.hasData()) {
                    final AppearanceManager appearanceManager = new AppearanceManager(player);
                    final boolean needsASkinChange = profile.getSkin() != null && !profile.getSkin().equals(player.getName());
                    final ActionResult actionResult = appearanceManager.updatePlayer(needsASkinChange, false);
                    if (!actionResult.isError()) {
                        player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Restore.OK));
                    } else {
                        player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Restore.ERROR, i18n.translatePrefixless(actionResult.getErrorKey())));
                    }
                }
            });

            for (Player online : Bukkit.getOnlinePlayers()) {
                final AppearanceManager appearanceManager = new AppearanceManager(online);
                appearanceManager.updateForOthers();
            }
        }, 20L);
    }
}
