package net.artelnatif.nicko.bungee.pluginchannel;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;

public class PluginMessageUtils {
    public static void sendMessage(final ServerInfo info, final String channel, final String... data) {
        if (info == null) { return; }

        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(channel);
        for (String elt : data) {
            output.writeUTF(elt);
        }
        System.out.printf("(%s) PluginMessage <-> %s", info.getSocketAddress().toString(), output);
        info.sendData(channel, output.toByteArray(), true);
    }
}
