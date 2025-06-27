package xyz.ineanto.nicko.packet;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.ineanto.nicko.profile.NickoProfile;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class PacketEventsPacketSender implements PacketSender {
    private final Player player;
    private final NickoProfile profile;

    public PacketEventsPacketSender(Player player, NickoProfile profile) {
        this.player = player;
        this.profile = profile;
    }

    @Override
    public void sendEntityRespawn() {
        if (!profile.hasData()) return;

        // TODO (Ineanto, 27/06/2025): Create/Delete packets here

        Bukkit.getOnlinePlayers().stream().filter(receiver -> receiver.getUniqueId() != player.getUniqueId()).forEach(receiver -> {
            // TODO (Ineanto, 27/06/2025): Send packets
            //sendPacket(destroy, player);
            //sendPacket(create, player);
        });
    }

    @Override
    public ActionResult updatePlayerProfile(String name) {
        final PlayerProfile playerProfile = new CraftPlayerProfile(player.getUniqueId(), name);
        // Copy previous properties to preserve skin
        playerProfile.setProperties(playerProfile.getProperties());
        player.setPlayerProfile(playerProfile);
        return ActionResult.ok();
    }

    @Override
    public ActionResult updatePlayerProfileProperties() {
        // TODO (Ineanto, 27/06/2025): Player profile

        try {
            final MojangAPI mojangAPI = Nicko.getInstance().getMojangAPI();

            final Optional<String> uuid = mojangAPI.getUUID(profile.getSkin());
            if (uuid.isEmpty()) {
                return ActionResult.error(LanguageKey.Error.MOJANG);
            }

            final Optional<MojangSkin> skin = mojangAPI.getSkin(uuid.get());
            if (skin.isEmpty()) {
                return ActionResult.error(LanguageKey.Error.MOJANG);
            }

            final MojangSkin skinResult = skin.get();
            //playerProfile.setProperties(skinResult.asProfileProperties());
            //player.setPlayerProfile(playerProfile);
            return ActionResult.ok();
        } catch (ExecutionException | IOException e) {
            return ActionResult.error(LanguageKey.Error.CACHE);
        }
    }

    @Override
    public void sendEntityMetadataUpdate() {
        // TODO (Ineanto, 27/06/2025): Entity Metadata packet
        //sendPacket(data, player);
    }

    @Override
    public void sendPlayerRespawn() {
        // TODO (Ineanto, 27/06/2025): Respawn packet
        //sendPacket(respawn, player);
    }

    @Override
    public void sendTabListUpdate(String displayName) {
        // TODO (Ineanto, 27/06/2025): TabList packet

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            //sendPacket(remove, onlinePlayer);
            //sendPacket(update, onlinePlayer);
        });
    }

    private void sendPacket(PacketWrapper<?> packet, Player player) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
}