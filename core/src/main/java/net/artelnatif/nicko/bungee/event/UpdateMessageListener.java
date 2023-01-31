package net.artelnatif.nicko.bungee.event;

import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.bungee.message.MessageDecoder;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class UpdateMessageListener implements Listener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(NickoBungee.PROXY_UPDATE)) { return; }

        try (DataInputStream input = new DataInputStream(new ByteArrayInputStream(event.getData()))) {
            final NickoProfile profile = MessageDecoder.decode(input);

            // TODO: 1/28/23 STORE PROFILE
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
