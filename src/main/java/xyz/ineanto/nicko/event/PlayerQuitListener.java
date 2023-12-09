package xyz.ineanto.nicko.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.gui.PlayerCheckGUI;
import xyz.ineanto.nicko.gui.PlayerCheckGUIData;
import xyz.xenondevs.invui.window.Window;
import xyz.xenondevs.invui.window.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final ActionResult result = NickoBukkit.getInstance().getDataStore().saveData(player);
        if (result.isError()) {
            NickoBukkit.getInstance().getLogger().warning("Failed to save data for " + player.getName());
        }

        // This is a dirty way to do it but could be worse tbh
        @SuppressWarnings("unchecked") final ArrayList<UUID> viewers = (ArrayList<UUID>) PlayerCheckGUIData.VIEWERS.clone();
        viewers.forEach(uuid -> {
            final Player windowWatcher = Bukkit.getPlayer(uuid);
            final Window openWindow = WindowManager.getInstance().getOpenWindow(windowWatcher);
            if (openWindow != null) {
                final List<? extends Player> playersWithoutOffline = Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(online -> online.getUniqueId() != player.getUniqueId()).collect(Collectors.toList());
                final PlayerCheckGUI gui = new PlayerCheckGUI(windowWatcher, playersWithoutOffline);
                openWindow.close();
                gui.open();
            }
        });
    }
}
