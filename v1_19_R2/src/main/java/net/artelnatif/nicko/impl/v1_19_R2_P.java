package net.artelnatif.nicko.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.Multimap;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class v1_19_R2_P implements InternalsProtocolLib {
    @Override
    public void updateSelf(Player player) {
        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final ServerLevel level = serverPlayer.getLevel();
        final ResourceKey<Level> levelResourceKey = serverPlayer.getLevel().dimension();
        final CraftWorld world = level.getWorld();
        // last boolean is: "has death location" attribute, if true, the optional contains the death dimension and position.
        // with the boolean being false, we don't need to provide a value, and thus we return an empty optional.
        final ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(serverPlayer.level.dimensionTypeId(),
                levelResourceKey, world.getSeed(),
                serverPlayer.gameMode.getPreviousGameModeForPlayer(), serverPlayer.gameMode.getGameModeForPlayer(),
                level.isDebug(),
                level.isFlat(),
                (byte) 0x00,
                Optional.empty());

        final boolean wasFlying = player.isFlying();
        serverPlayer.connection.send(respawn);
        player.setFlying(wasFlying);
        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.updateInventory();
    }

    @Override
    public void updateOthers(Player player) {
        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final ClientboundRemoveEntitiesPacket remove = new ClientboundRemoveEntitiesPacket(serverPlayer.getBukkitEntity().getEntityId());
        final ClientboundAddEntityPacket add = new ClientboundAddEntityPacket(serverPlayer);

        /*
          BIT MASKS:
          0x01 	Cape enabled
          0x02 	Jacket enabled
          0x04 	Left sleeve enabled
          0x08 	Right sleeve enabled
          0x10 	Left pants leg enabled
          0x20 	Right pants leg enabled
          0x40 	Hat enabled
         */
        final SynchedEntityData entityData = serverPlayer.getEntityData();
        final EntityDataAccessor<Byte> skinPartAccessor = new EntityDataAccessor<>(17, EntityDataSerializers.BYTE);
        entityData.set(skinPartAccessor, (byte) 0x7f); // 127, all masks combined
        final ClientboundSetEntityDataPacket entityMetadata = new ClientboundSetEntityDataPacket(serverPlayer.getBukkitEntity().getEntityId(), entityData.getNonDefaultValues());

        Bukkit.getOnlinePlayers().forEach(online -> {
            ServerPlayer onlineServerPlayer = ((CraftPlayer) online).getHandle();
            if (onlineServerPlayer.getBukkitEntity().getUniqueId() != player.getUniqueId()) {
                onlineServerPlayer.connection.send(remove);
                onlineServerPlayer.connection.send(add);
            }
            onlineServerPlayer.connection.send(entityMetadata);
        });
    }

    @Override
    public ActionResult updateProfile(Player player, NickoProfile profile, boolean skinChange, boolean reset) {
        final boolean changeOnlyName = profile.getSkin() != null && !profile.getSkin().equalsIgnoreCase(player.getName());
        final String profileName = profile.getName() == null ? player.getName() : profile.getName();
        Optional<MojangSkin> skin;

        final WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(player).withName(profileName);
        if (skinChange || changeOnlyName) {
            try {
                final MojangAPI mojang = NickoBukkit.getInstance().getMojangAPI();
                final Optional<String> uuid = mojang.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = (reset ? mojang.getSkinWithoutCaching(uuid.get()) : mojang.getSkin(uuid.get()));
                    if (skin.isPresent()) {
                        final Multimap<String, WrappedSignedProperty> properties = gameProfile.getProperties();
                        properties.removeAll("textures");
                        properties.put("textures", new WrappedSignedProperty("textures", skin.get().value(), skin.get().signature()));
                        updateSelf(player);
                    } else {
                        return new ActionResult(I18NDict.Error.SKIN_FAIL_MOJANG);
                    }
                } else {
                    return new ActionResult(I18NDict.Error.NAME_FAIL_MOJANG);
                }
            } catch (ExecutionException e) {
                return new ActionResult(I18NDict.Error.SKIN_FAIL_CACHE);
            } catch (IOException e) {
                return new ActionResult(I18NDict.Error.NAME_FAIL_MOJANG);
            }
        }

        // Letting ProtocolLib handle the reflection here.
        final PacketContainer remove = getProtocolLib().createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        remove.getUUIDLists().write(0, Collections.singletonList(player.getUniqueId()));

        final EnumSet<EnumWrappers.PlayerInfoAction> actions = EnumSet.of(
                EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.UPDATE_LATENCY,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED);
        final PacketContainer add = getProtocolLib().createPacket(PacketType.Play.Server.PLAYER_INFO);

        add.getPlayerInfoActions().write(0, actions);
        add.getPlayerInfoDataLists().write(1, Collections.singletonList(new PlayerInfoData(
                gameProfile,
                player.getPing(),
                EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                WrappedChatComponent.fromText(profileName)
        )));

        Bukkit.getOnlinePlayers().forEach(online -> {
            getProtocolLib().sendServerPacket(online, remove);
            getProtocolLib().sendServerPacket(online, add);
        });
        updateOthers(player);
        return new ActionResult();
    }
}