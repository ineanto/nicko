package net.artelnatif.nicko.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
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
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class v1_19_R2 implements Internals {
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

        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final GameProfile gameProfile = serverPlayer.getGameProfile();
        final GameProfile newGameProfile = new GameProfile(player.getUniqueId(), profileName);

        final ClientboundPlayerInfoRemovePacket remove = new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId()));
        // TODO: 1/20/23 Sets Gamemode to Survival but keeps the flying? Visual effect only?
        //final ClientboundPlayerInfoUpdatePacket init = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(serverPlayer));

        if (skinChange || changeOnlyName) {
            try {
                final MojangAPI mojang = NickoBukkit.getInstance().getMojangAPI();
                final Optional<String> uuid = mojang.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = (reset ? mojang.getSkinWithoutCaching(uuid.get()) : mojang.getSkin(uuid.get()));
                    if (skin.isPresent()) {
                        final PropertyMap properties = newGameProfile.getProperties();
                        properties.removeAll("textures");
                        properties.put("textures", new Property("textures", skin.get().value(), skin.get().signature()));
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

        final ClientboundPlayerInfoUpdatePacket init = new ClientboundPlayerInfoUpdatePacket(
                EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                        ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY,
                        ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED),
                Collections.singletonList(serverPlayer));

        Field field;
        try {
            field = init.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(init, List.of(new ClientboundPlayerInfoUpdatePacket.Entry(
                    player.getUniqueId(),
                    newGameProfile,
                    true,
                    serverPlayer.latency,
                    serverPlayer.gameMode.getGameModeForPlayer(),
                    Component.literal(profileName),
                    serverPlayer.getChatSession().asData()
            )));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        System.out.println("======= AFTER ");
        System.out.println("init.entries().toString() = " + init.entries().toString());

        serverPlayer.connection.send(remove);
        serverPlayer.connection.send(init);
        Bukkit.getOnlinePlayers().forEach(online -> {
            ServerPlayer onlineEntityPlayer = ((CraftPlayer) online).getHandle();
            onlineEntityPlayer.connection.send(remove);
            onlineEntityPlayer.connection.send(init);
        });
        updateOthers(player);
        return new ActionResult();
    }
}
