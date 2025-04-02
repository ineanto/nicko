package xyz.ineanto.nicko.packet;

import xyz.ineanto.nicko.appearance.ActionResult;

public interface PacketSender {
    void sendEntityRespawn();

    ActionResult updatePlayerProfile(String name);

    ActionResult updatePlayerProfileProperties();

    void sendEntityMetadataUpdate();

    void sendPlayerRespawn();

    void sendTabListUpdate(String displayName);
}
