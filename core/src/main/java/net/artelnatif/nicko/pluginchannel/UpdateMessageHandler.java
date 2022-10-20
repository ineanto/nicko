package net.artelnatif.nicko.pluginchannel;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.artelnatif.nicko.bungee.NickoBungee;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class UpdateMessageHandler implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals(NickoBungee.NICKO_PLUGIN_CHANNEL_UPDATE)) {
            return;
        }

        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        final String subchannel = in.readUTF();
        if(subchannel.equals(NickoBungee.NICKO_PLUGIN_CHANNEL_UPDATE)) {
            // TODO: 10/20/22 update player
        }
    }
}
