package net.artelnatif.nicko.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.reflect.Field;
import java.util.*;

public class v1_19_R2 implements Internals {
    @Override
    public void updateSelf(Player player) {
        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final ServerLevel level = serverPlayer.getLevel();
        final ResourceKey<Level> levelResourceKey = serverPlayer.getLevel().dimension();
        final ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(serverPlayer.level.dimensionTypeId(),
                levelResourceKey, level.getWorld().getSeed(),
                serverPlayer.gameMode.getGameModeForPlayer(), serverPlayer.gameMode.getPreviousGameModeForPlayer(),
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

        final SynchedEntityData entityData = serverPlayer.getEntityData();
        final EntityDataAccessor<Byte> skinPartAccessor = new EntityDataAccessor<>(17, EntityDataSerializers.BYTE);
        entityData.set(skinPartAccessor, (byte) 0x7f);
        final ClientboundSetEntityDataPacket entityMetadata = new ClientboundSetEntityDataPacket(serverPlayer.getBukkitEntity().getEntityId(), entityData.getNonDefaultValues());

        Bukkit.getOnlinePlayers().forEach(online -> {
            final ServerPlayer onlineServerPlayer = ((CraftPlayer) online).getHandle();
            if (onlineServerPlayer.getBukkitEntity().getUniqueId() != player.getUniqueId()) {
                onlineServerPlayer.connection.send(remove);
                onlineServerPlayer.connection.send(add);
            }
            onlineServerPlayer.connection.send(entityMetadata);
        });
    }

    @Override
    public ActionResult<Void> updateProfile(Player player, NickoProfile profile, boolean skinChange, boolean reset) {
        final boolean changeOnlyName = profile.getSkin() != null && !profile.getSkin().equalsIgnoreCase(player.getName());
        final String profileName = profile.getName() == null ? player.getName() : profile.getName();

        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        final GameProfile gameProfile = new GameProfile(player.getUniqueId(), profileName);

        if (skinChange || changeOnlyName) {
            final ActionResult<MojangSkin> skinFetch = fetchSkinTextures(profile, reset);
            if (!skinFetch.isError()) {
                final MojangSkin skin = skinFetch.getResult();
                final PropertyMap properties = gameProfile.getProperties();
                properties.removeAll("textures");
                properties.put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
                updateSelf(player);
            }
        }

        final ClientboundPlayerInfoUpdatePacket init = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.singletonList(serverPlayer));
        final ClientboundPlayerInfoRemovePacket remove = new ClientboundPlayerInfoRemovePacket(Collections.singletonList(player.getUniqueId()));

        RemoteChatSession chatSession;
        if (serverPlayer.getChatSession() == null) {
            NickoBukkit.getInstance().getLogger().warning("Chat Session of " + serverPlayer.displayName + " is null!");
            NickoBukkit.getInstance().getLogger().warning("If your server is in offline mode/under BungeeCord you can safely ignore this message.");
            chatSession = null;
        } else {
            final UUID uuid = serverPlayer.getChatSession().sessionId();
            final ProfilePublicKey ppk = serverPlayer.getChatSession().profilePublicKey();
            chatSession = new RemoteChatSession(uuid, ppk);
        }

        spoofPlayerInfoPacket(init, Collections.singletonList(new ClientboundPlayerInfoUpdatePacket.Entry(
                player.getUniqueId(),
                gameProfile,
                true,
                serverPlayer.latency,
                serverPlayer.gameMode.getGameModeForPlayer(),
                Component.literal(profileName),
                chatSession == null ? null : chatSession.asData()
        )));

        Bukkit.getOnlinePlayers().forEach(online -> {
            final ServerPlayer onlinePlayer = ((CraftPlayer) online).getHandle();
            onlinePlayer.connection.send(remove);
            onlinePlayer.connection.send(init);
        });
        updateOthers(player);
        return new ActionResult<>();
    }

    private void spoofPlayerInfoPacket(Object object, Object newValue) {
        try {
            final Field field = object.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(object, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            NickoBukkit.getInstance().getLogger().warning("Unable to spoof packet, that's bad! (" + e.getMessage() + ")");
        }
    }
}
