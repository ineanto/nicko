package net.artelnatif.nicko.bukkit.pluginchannel;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.bungee.NickoBungee;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;

public class PluginMessageHandler implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(NickoBungee.SERVER_DATA)) {
            return;
        }

        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        final int payloadSize = in.readInt();
        if (payloadSize == 0 || payloadSize > 4) {
            NickoBukkit.getInstance().getLogger().severe("Prevented error by skipping malformed payload of size " + payloadSize + "!");
            NickoBukkit.getInstance().getLogger().severe("This should not have happened, open an issue at https://atnrch.xyz/git/aro/Nicko !");
            return;
        }

        final ArrayList<String> decodedPayload = new ArrayList<>(payloadSize);
        for (int i = 0; i < payloadSize; i++) {
            decodedPayload.add(in.readUTF());
        }

        System.out.println("decodedPayload = " + decodedPayload);
    }
}
