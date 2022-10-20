package net.artelnatif.nicko.bungee.event;

import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.bungee.pluginchannel.PluginChannelHelper;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {
    
    /*
     * Nicko Message Format
     * FETCH: nicko:skin/fetch
     * - UUID
     *
     * UPDATE: nicko:skin/update
     * - UUID
     * - Skin
     * - Name
     */

    @EventHandler
    public void onSwitch(ServerSwitchEvent event) {
        final ServerInfo from = event.getFrom();
        final ProxiedPlayer player = event.getPlayer();
        final Server to = player.getServer();
        PluginChannelHelper.sendMessage(from, NickoBungee.NICKO_PLUGIN_CHANNEL_FETCH, player.getUniqueId().toString());
    }
}
