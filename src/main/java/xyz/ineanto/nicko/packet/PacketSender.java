package xyz.ineanto.nicko.packet;

import xyz.ineanto.nicko.appearance.ActionResult;

public interface PacketSender {
    void sendEntityRespawn();

    ActionResult sendGameProfileUpdate(String name, boolean skinChange, boolean reset);

    void sendEntityMetadataUpdate();

    void sendPlayerRespawn();

    void sendTabListUpdate(String displayName);
}
