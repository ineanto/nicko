package net.artelnatif.nicko.bungee.event;

import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.bungee.pluginchannel.PluginMessageUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;

public class ServerSwitchListener implements Listener {
    @EventHandler
    public void onSwitch(ServerSwitchEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final Server server = player.getServer();
        System.out.println("NickoBungee.NICKO_PLUGIN_CHANNEL_UPDATE = " + NickoBungee.NICKO_PLUGIN_CHANNEL_UPDATE);

        final ArrayList<String> payload = new ArrayList<>();
        payload.add(player.getUniqueId().toString());

        PluginMessageUtils.sendMessage(server.getInfo(), NickoBungee.NICKO_PLUGIN_CHANNEL_UPDATE, payload);
    }
}
