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
import xyz.ineanto.nicko.gui.PlayerCheckGUI;
import xyz.ineanto.nicko.gui.PlayerCheckGUIData;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.ineanto.nicko.storage.name.PlayerNameStore;
import xyz.xenondevs.invui.window.Window;
import xyz.xenondevs.invui.window.WindowManager;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class PlayerJoinListener implements Listener {
    private final Logger logger = Logger.getLogger("PlayerJoinListener");

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final NickoBukkit instance = NickoBukkit.getInstance();
        final I18N i18n = new I18N(player);
        final PlayerNameStore nameStore = instance.getNameStore();
        final PlayerDataStore dataStore = instance.getDataStore();
        nameStore.storeName(player);

        final Optional<NickoProfile> optionalProfile = dataStore.getData(player.getUniqueId());
        optionalProfile.ifPresent(profile -> {
            // Random Skin on connection feature
            if (profile.isRandomSkin()) {
                final String name = instance.getNameFetcher().getRandomUsername();
                final String skin = instance.getNameFetcher().getRandomUsername();
                profile.setName(name);
                profile.setSkin(skin);
                dataStore.updateCache(player.getUniqueId(), profile);
            }

            if (profile.hasData()) {
                final AppearanceManager appearanceManager = new AppearanceManager(player);
                final boolean needsASkinChange = profile.getSkin() != null && !profile.getSkin().equals(player.getName());
                final ActionResult actionResult = appearanceManager.updatePlayer(needsASkinChange, false);
                if (!actionResult.isError()) {
                    player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Restore.OK, true));
                } else {
                    player.sendMessage(
                            i18n.translate(I18NDict.Event.Appearance.Restore.ERROR,
                                    true,
                                    i18n.translate(actionResult.getErrorKey(), false)
                            ));
                }
            }
        });

        for (Player online : Bukkit.getOnlinePlayers().stream().filter(op -> op.getUniqueId() != player.getUniqueId()).toList()) {
            final Optional<NickoProfile> optionalOnlinePlayerProfile = dataStore.getData(online.getUniqueId());

            optionalOnlinePlayerProfile.ifPresent(profile -> {
                final AppearanceManager appearanceManager = new AppearanceManager(online);
                final boolean needsASkinChange = profile.getSkin() != null && !profile.getSkin().equals(online.getName());
                final ActionResult actionResult = appearanceManager.updateForOthers(needsASkinChange, false);
                if (actionResult.isError()) {
                    logger.warning("Something wrong happened while updating players to joining player (" + actionResult.getErrorKey() + ")");
                }
            });
        }

        @SuppressWarnings("unchecked") final ArrayList<UUID> viewers = (ArrayList<UUID>) PlayerCheckGUIData.VIEWERS.clone();
        viewers.forEach(uuid -> {
            final Player windowWatcher = Bukkit.getPlayer(uuid);
            final Window openWindow = WindowManager.getInstance().getOpenWindow(windowWatcher);
            if (openWindow != null) {
                final PlayerCheckGUI gui = new PlayerCheckGUI(windowWatcher, Bukkit.getOnlinePlayers());
                openWindow.close();
                gui.open();
            }
        });
    }
}
