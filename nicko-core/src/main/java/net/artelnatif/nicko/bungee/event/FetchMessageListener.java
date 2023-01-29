package net.artelnatif.nicko.bungee.event;

import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.bungee.message.PluginMessageSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class FetchMessageListener implements Listener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(NickoBungee.PROXY_FETCH)) { return; }

        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()))) {
            final ProxyServer proxy = NickoBungee.getInstance().getProxy();

            final String uuid = input.readUTF();
            final ProxiedPlayer player = proxy.getPlayer(UUID.fromString(uuid));
            final ServerInfo serverInfo = player.getServer().getInfo();

            // TODO: 1/28/23 FETCH PROFILE
            final ArrayList<String> payload = new ArrayList<>();
            payload.add(player.getUniqueId().toString());

            PluginMessageSender.send(serverInfo, NickoBungee.SERVER_DATA, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
