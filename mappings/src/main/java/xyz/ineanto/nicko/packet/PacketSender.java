package xyz.ineanto.nicko.packet;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.action.ActionResult;

public abstract class PacketSender {
    protected final Player player;
    protected final String name;
    protected final String skin;

    public PacketSender(Player player, String name, String skin) {
        this.player = player;
        this.name = name;
        this.skin = skin;
    }

    public abstract void sendEntityRespawn();

    public abstract ActionResult sendGameProfileUpdate(String name, boolean skinChange, boolean reset);

    public abstract void sendEntityMetadataUpdate();

    public abstract void sendPlayerRespawn();

    public abstract void sendTabListUpdate(String displayName);
}
