package xyz.ineanto.nicko.packet;

import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.ineanto.nicko.packet.wrapper.*;
import xyz.ineanto.nicko.profile.NickoProfile;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class WrapperPacketSender implements PacketSender {
    private final Player player;
    private final NickoProfile profile;

    private WrappedGameProfile gameProfile;

    public WrapperPacketSender(Player player, NickoProfile profile) {
        this.player = player;
        this.profile = profile;
    }

    @Override
    public void sendEntityRespawn() {
        if (!profile.hasData()) return;

        final WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
        final WrapperPlayServerSpawnEntity spawn = new WrapperPlayServerSpawnEntity();
        destroy.setEntityIds(IntList.of(player.getEntityId()));
        spawn.setEntityId(player.getEntityId());
        spawn.setLocation(player.getLocation());
        spawn.setPlayerId(player.getUniqueId());
        Bukkit.getOnlinePlayers().stream().filter(receiver -> receiver.getUniqueId() != player.getUniqueId()).forEach(receiver -> {
            destroy.sendPacket(receiver);
            spawn.sendPacket(receiver);
        });
    }

    @Override
    public ActionResult sendGameProfileUpdate(String name, boolean skinChange, boolean reset) {
        this.gameProfile = WrappedGameProfile.fromPlayer(player).withName(name);

        // TODO (Ineanto, 31/10/2024): Could get refactored to omit this boolean?
        if (skinChange) {
            Optional<MojangSkin> skin;
            try {
                final MojangAPI mojangAPI = Nicko.getInstance().getMojangAPI();
                final Optional<String> uuid = mojangAPI.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = reset ? mojangAPI.getSkinWithoutCaching(uuid.get()) : mojangAPI.getSkin(uuid.get());
                    if (skin.isPresent()) {
                        final MojangSkin skinResult = skin.get();
                        final Multimap<String, WrappedSignedProperty> properties = gameProfile.getProperties();
                        properties.get("textures").clear();
                        properties.put("textures", new WrappedSignedProperty("textures", skinResult.value(), skinResult.signature()));
                    } else {
                        return ActionResult.error(LanguageKey.Error.MOJANG_SKIN);
                    }
                } else {
                    return ActionResult.error(LanguageKey.Error.MOJANG_NAME);
                }
                return ActionResult.ok();
            } catch (ExecutionException e) {
                return ActionResult.error(LanguageKey.Error.CACHE);
            } catch (IOException e) {
                return ActionResult.error(LanguageKey.Error.MOJANG_NAME);
            } catch (InterruptedException e) {
                return ActionResult.error("Unknown error");
            }
        }
        return ActionResult.ok();
    }


    @Override
    public void sendEntityMetadataUpdate() {
        final WrappedDataWatcher entityWatcher = WrappedDataWatcher.getEntityWatcher(player);
        entityWatcher.setObject(17, (byte) 0x7f, true);
    }

    @Override
    public void sendPlayerRespawn() {
        final World world = player.getWorld();

        final WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn();
        respawn.setDimension(world);
        respawn.setSeed(world.getSeed());
        respawn.setGameMode(player.getGameMode());
        respawn.setPreviousGameMode(player.getGameMode());
        respawn.setCopyMetadata(true);
        respawn.sendPacket(player);
    }

    @Override
    public void sendTabListUpdate(String displayName) {
        if (gameProfile == null) {
            Nicko.getInstance().getLogger().warning("Hello. I sincerely hope you're doing great out there.");
            Nicko.getInstance().getLogger().warning("If you see this message, I've failed at my task and I'm a terrible programmer.");
            Nicko.getInstance().getLogger().warning("Report this issue on https://git.ineanto.xyz/ineanto/nicko, thank you!");
            return;
        }

        final WrapperPlayerServerPlayerInfo add = new WrapperPlayerServerPlayerInfo();
        final WrapperPlayerServerPlayerInfoRemove remove = new WrapperPlayerServerPlayerInfoRemove();
        final EnumSet<EnumWrappers.PlayerInfoAction> actions = EnumSet.of(
                EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.INITIALIZE_CHAT,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
                EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME,
                EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE,
                EnumWrappers.PlayerInfoAction.UPDATE_LATENCY);
        remove.setUUIDs(List.of(player.getUniqueId()));
        remove.broadcastPacket();
        add.setActions(actions);

        add.setData(List.of(new PlayerInfoData(
                player.getUniqueId(),
                player.getPing(),
                true,
                EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                gameProfile,
                WrappedChatComponent.fromText(displayName),
                WrappedRemoteChatSessionData.fromPlayer(player)
        )));

        add.broadcastPacket();
    }
}
