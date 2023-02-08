package net.artelnatif.nicko.bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;

public class PluginMessageSender {
    public static void send(final ServerInfo info, final String channel, final ArrayList<String> payload) {
        if (info == null) { return; }

        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeInt(payload.size());
        for (String elt : payload) {
            output.writeUTF(elt);
        }
        info.sendData(channel, output.toByteArray(), true);
    }
}
