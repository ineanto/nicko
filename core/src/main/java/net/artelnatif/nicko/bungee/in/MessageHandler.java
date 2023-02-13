package net.artelnatif.nicko.bungee.in;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.artelnatif.nicko.Nicko;
import net.artelnatif.nicko.bungee.NickoBungee;
import net.artelnatif.nicko.bungee.PluginMessageSender;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.cache2k.Cache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;

public class MessageHandler implements Listener {
    @EventHandler
    public void onMessage(PluginMessageEvent event) {
        final Cache<UUID, NickoProfile> cache = NickoBungee.getInstance().getProfileCache();
        if (event.getTag().equalsIgnoreCase(Nicko.MESSAGE_UPDATE)) {
            try {
                final ByteArrayInputStream inputStream = new ByteArrayInputStream(event.getData());
                final ObjectInputStream objectStream = new ObjectInputStream(inputStream);

                final String uuid = objectStream.readUTF();
                final NickoProfile profile = (NickoProfile) objectStream.readObject();
                cache.put(UUID.fromString(uuid), profile);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (event.getTag().equalsIgnoreCase(Nicko.MESSAGE_FETCH)) {
            final ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
            final String stringUuid = input.readUTF();
            final UUID uuid = UUID.fromString(stringUuid);
            final ServerInfo info = NickoBungee.getInstance().getProxy().getPlayer(uuid).getServer().getInfo();
            final NickoProfile profile = cache.peekAndRemove(uuid);
            PluginMessageSender.sendObject(info, Nicko.MESSAGE_FETCH, profile);
        }
    }
}
