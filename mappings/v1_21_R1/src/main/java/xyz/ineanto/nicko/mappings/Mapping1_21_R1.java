package xyz.ineanto.nicko.mappings;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.action.ActionResult;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.ineanto.nicko.packet.PacketSender;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Mapping1_21_R1 extends PacketSender {

    public Mapping1_21_R1(Player player, String name, String skin) {
        super(player, name, skin);
    }

    @Override
    public void sendEntityRespawn() {
        if (name == null || name.isEmpty() || skin == null || skin.isEmpty()) return;

        final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(IntList.of(player.getEntityId()));
        final PacketPlayOutSpawnEntity add = new PacketPlayOutSpawnEntity(
                new Random().nextInt(9999),
                player.getUniqueId(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                player.getLocation().getPitch(),
                player.getLocation().getYaw(),
                EntityTypes.bS,
                0,
                Vec3D.c,
                player.getEyeLocation().getYaw()
        );

        Bukkit.getOnlinePlayers().stream().filter(receiver -> receiver.getUniqueId() != player.getUniqueId()).forEach(receiver -> {
            sendPacket(destroy, player);
            sendPacket(add, player);
        });
    }

    @Override
    public ActionResult sendGameProfileUpdate(String name, boolean skinChange, boolean reset) {
        final GameProfile gameProfile = new GameProfile(player.getUniqueId(), name);

        // TODO (Ineanto, 31/10/2024): Could this be refactored to get rid of the boolean?
        if (skinChange) {
            Optional<MojangSkin> skin;
            try {
                final MojangAPI mojang = new MojangAPI();
                final Optional<String> uuid = mojang.getUUID(skin);
                if (uuid.isPresent()) {
                    skin = reset ? mojang.getSkinWithoutCaching(uuid.get()) : mojang.getSkin(uuid.get());
                    if (skin.isPresent()) {
                        final MojangSkin skinResult = skin.get();
                        final PropertyMap properties = gameProfile.getProperties();
                        properties.get("textures").clear();
                        properties.put("textures", new Property("textures", skinResult.value(), skinResult.signature()));
                        ((CraftPlayer) player).getHandle().gameProfile = gameProfile;
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
        final SynchedEntityData.DataValue<?> dataValueComponent =
                new SynchedEntityData.DataItem<>(
                        new EntityDataAccessor<>(17, EntityDataSerializers.BYTE),
                        (byte) 0x7f
                ).value();

        final ClientboundSetEntityDataPacket data = new ClientboundSetEntityDataPacket(player.getEntityId(), List.of(dataValueComponent));
        sendPacket(data, player);
    }

    @Override
    public void sendPlayerRespawn() {
        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final ServerLevel level = serverPlayer.serverLevel();

        final ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(serverPlayer.createCommonSpawnInfo(level), (byte) 0x03);
        sendPacket(respawn, player);
    }

    @Override
    public void sendTabListUpdate(String displayName) {
        final ServerPlayer serverPlayer = (((CraftPlayer) player)).getHandle();

        final EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.of(
                ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT,
                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE,
                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY);

        final List<ClientboundPlayerInfoUpdatePacket.Entry> entries = List.of(new ClientboundPlayerInfoUpdatePacket.Entry(
                serverPlayer.getUUID(),
                serverPlayer.gameProfile,
                true,
                serverPlayer.connection.latency(),
                serverPlayer.gameMode.getGameModeForPlayer(),
                MutableComponent.create(new PlainTextContents.LiteralContents(displayName)),
                Optionull.map(serverPlayer.getChatSession(), RemoteChatSession::asData)
        ));

        final ClientboundPlayerInfoUpdatePacket update = new ClientboundPlayerInfoUpdatePacket(actions, entries);
        final ClientboundPlayerInfoRemovePacket remove = new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId()));

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            sendPacket(remove, onlinePlayer);
            sendPacket(update, onlinePlayer);
        });
    }

    private void sendPacket(Packet<?> packet, Player player) {
        (((CraftPlayer) player).getHandle()).f.a(packet);
    }
}