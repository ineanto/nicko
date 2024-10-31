package xyz.ineanto.nicko.packet;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.ineanto.nicko.profile.NickoProfile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Look at this Mojang.
 * I want you to really stare at this code.
 * You made me do this.
 */
public class InternalPacketSender implements PacketSender {
    private final Player player;
    private final NickoProfile profile;

    public InternalPacketSender(Player player, NickoProfile profile) {
        this.player = player;
        this.profile = profile;
    }

    @Override
    public void sendEntityRespawn() {
        if (!profile.hasData()) return;

        final Entity entityPlayer = (Entity) player;

        final ClientboundRemoveEntitiesPacket destroy = new ClientboundRemoveEntitiesPacket(IntList.of(player.getEntityId()));
        final ClientboundAddEntityPacket add = new ClientboundAddEntityPacket(entityPlayer, 0, entityPlayer.getOnPos());

        Bukkit.getOnlinePlayers().stream().filter(receiver -> receiver.getUniqueId() != player.getUniqueId()).forEach(receiver -> {
            sendPacket(destroy, player);
            sendPacket(add, player);
        });
    }

    @Override
    public ActionResult sendGameProfileUpdate(String name, boolean skinChange, boolean reset) {
        final GameProfile gameProfile = ((ServerPlayer) player).gameProfile;

        // TODO (Ineanto, 31/10/2024): Could this be refactored to get rid of the boolean?
        if (skinChange) {
            Optional<MojangSkin> skin;
            try {
                final MojangAPI mojangAPI = Nicko.getInstance().getMojangAPI();
                final Optional<String> uuid = mojangAPI.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = reset ? mojangAPI.getSkinWithoutCaching(uuid.get()) : mojangAPI.getSkin(uuid.get());
                    if (skin.isPresent()) {
                        final MojangSkin skinResult = skin.get();
                        final PropertyMap properties = gameProfile.getProperties();
                        properties.get("textures").clear();
                        properties.put("textures", new Property("textures", skinResult.value(), skinResult.signature()));
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
        final ServerPlayer serverPlayer = (ServerPlayer) player;
        final ServerLevel world = (ServerLevel) player.getWorld();

        final ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(serverPlayer.createCommonSpawnInfo(world), (byte) 0x03);
        sendPacket(respawn, player);
    }

    @Override
    public void sendTabListUpdate(String displayName) {

    }

    private void sendPacket(Packet<?> packet, Player player) {
        ((ServerPlayer) player).connection.send(packet);
    }
}
