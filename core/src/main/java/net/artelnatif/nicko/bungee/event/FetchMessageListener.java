package net.artelnatif.nicko.bungee.event;

import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.bungee.message.PluginMessageSender;
import net.artelnatif.nicko.disguise.NickoProfile;
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
import java.util.Optional;
import java.util.UUID;

public class FetchMessageListener implements Listener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(NickoBungee.PROXY_FETCH)) { return; }

        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()))) {
            final NickoBungee instance = NickoBungee.getInstance();
            final ProxyServer proxy = instance.getProxy();

            final String stringUuid = input.readUTF();
            final UUID uuid = UUID.fromString(stringUuid);
            final ProxiedPlayer player = proxy.getPlayer(uuid);
            final ServerInfo serverInfo = player.getServer().getInfo();

            final Optional<NickoProfile> optionalProfile = instance.getNicko().getDataStore().getData(uuid);
            if (optionalProfile.isPresent()) {
                final NickoProfile profile = optionalProfile.get();
                final ArrayList<String> payload = new ArrayList<>();
                payload.add(player.getUniqueId().toString());
                payload.add(profile.getName());
                payload.add(profile.getSkin());
                payload.add(String.valueOf(profile.isBungeecordTransfer()));
                payload.add(profile.getLocale().getCode());

                PluginMessageSender.send(serverInfo, NickoBungee.SERVER_DATA, payload);
                return;
            }

            instance.getNicko().getLogger().warning("Unable to send profile to distant server!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
