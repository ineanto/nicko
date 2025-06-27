package xyz.ineanto.nicko.packet;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.ineanto.nicko.profile.NickoProfile;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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

        final WrapperPlayServerDestroyEntities destroy = new WrapperPlayServerDestroyEntities(player.getEntityId());
        final WrapperPlayServerSpawnEntity spawn = new WrapperPlayServerSpawnEntity(
                new Random().nextInt(9999),
                Optional.of(player.getUniqueId()),
                EntityTypes.PLAYER,
                new Vector3d(player.getX(), player.getY(), player.getZ()),
                player.getPitch(),
                player.getYaw(),
                player.getBodyYaw(),
                0,
                Optional.empty()
        );

        Bukkit.getOnlinePlayers().stream().filter(receiver -> receiver.getUniqueId() != player.getUniqueId()).forEach(receiver -> {
            sendPacket(destroy, player);
            sendPacket(spawn, player);
        });
    }

    @Override
    public ActionResult updatePlayerProfile(String name) {
        final PlayerProfile previousProfile = player.getPlayerProfile();
        final PlayerProfile newProfile = Bukkit.getServer().createProfile(player.getUniqueId(), name);

        // Copy previous properties to preserve skin
        newProfile.setProperties(previousProfile.getProperties());
        player.setPlayerProfile(newProfile);
        return ActionResult.ok();
    }

    @Override
    public ActionResult updatePlayerProfileProperties() {
        final PlayerProfile playerProfile = player.getPlayerProfile();

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
            playerProfile.setProperties(skinResult.asProfileProperties());
            player.setPlayerProfile(playerProfile);
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
        final World world = player.getWorld();

        final WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(
                SpigotConversionUtil.typeFromBukkitWorld(world),
                world.getName(),
                Difficulty.getById(world.getDifficulty().ordinal()),
                world.getSeed(),
                SpigotConversionUtil.fromBukkitGameMode(player.getGameMode()),
                SpigotConversionUtil.fromBukkitGameMode(player.getPreviousGameMode()),
                false,
                false,
                true,
                null,
                null,
                null
        );

        sendPacket(respawn, player);
    }

    @Override
    public void sendTabListUpdate(String displayName) {
        final EnumSet<WrapperPlayServerPlayerInfoUpdate.Action> actions = EnumSet.of(
                WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
                WrapperPlayServerPlayerInfoUpdate.Action.INITIALIZE_CHAT,
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED,
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME,
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_GAME_MODE,
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LATENCY
        );

        final List<WrapperPlayServerPlayerInfoUpdate.PlayerInfo> entries = List.of(
                new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                        toUserProfile(player.getPlayerProfile()),
                        true,
                        player.getPing(),
                        SpigotConversionUtil.fromBukkitGameMode(player.getGameMode()),
                        Component.text(displayName),
                        null,
                        player.getPlayerListOrder(),
                        true
                )
        );

        final WrapperPlayServerPlayerInfoRemove remove = new WrapperPlayServerPlayerInfoRemove(player.getUniqueId());
        final WrapperPlayServerPlayerInfoUpdate update = new WrapperPlayServerPlayerInfoUpdate(actions, entries);

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            sendPacket(remove, onlinePlayer);
            sendPacket(update, onlinePlayer);
        });
    }

    private UserProfile toUserProfile(PlayerProfile playerProfile) {
        return new UserProfile(
                playerProfile.getId(),
                playerProfile.getName(),
                playerProfile.getProperties()
                        .stream()
                        .map(profileProperty -> new TextureProperty(
                                profileProperty.getName(),
                                profileProperty.getValue(),
                                profileProperty.getSignature()
                        ))
                        .toList()
        );
    }

    private void sendPacket(PacketWrapper<?> packet, Player player) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
}