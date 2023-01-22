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

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;

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
        final GameProfile gameProfile = new GameProfile(player.getUniqueId(), profileName);

        final ClientboundPlayerInfoRemovePacket remove = new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId()));

        if (skinChange || changeOnlyName) {
            try {
                final MojangAPI mojang = NickoBukkit.getInstance().getMojangAPI();
                final Optional<String> uuid = mojang.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = (reset ? mojang.getSkinWithoutCaching(uuid.get()) : mojang.getSkin(uuid.get()));
                    if (skin.isPresent()) {
                        final PropertyMap properties = gameProfile.getProperties();
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
                        ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT,
                        ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY,
                        ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED),
                Collections.singletonList(serverPlayer));

        if (serverPlayer.getChatSession() == null) {
            NickoBukkit.getInstance().getLogger().warning("Chat Session of " + serverPlayer.displayName + " is undefined." +
                                                          "Nicko might fail at changing skins or might throw an error." +
                                                          "Worse however, the player might get kicked when chatting." +
                                                          "This is pretty rare however and this" +
                                                          "warning can be safely ignored in most cases.");
        }

        final UUID uuid = serverPlayer.getChatSession().sessionId();
        final ProfilePublicKey ppk = serverPlayer.getChatSession().profilePublicKey();
        final RemoteChatSession newChatSession = new RemoteChatSession(uuid, ppk);

        spoofPlayerInfoPacket(init, List.of(new ClientboundPlayerInfoUpdatePacket.Entry(
                player.getUniqueId(),
                gameProfile,
                true,
                serverPlayer.latency,
                serverPlayer.gameMode.getGameModeForPlayer(),
                Component.literal(profileName),
                newChatSession.asData()
        )));

        Bukkit.getOnlinePlayers().forEach(online -> {
            final ServerPlayer onlinePlayer = ((CraftPlayer) online).getHandle();
            onlinePlayer.connection.send(remove);
            onlinePlayer.connection.send(init);
        });
        updateOthers(player);
        return new ActionResult();
    }

    private void spoofPlayerInfoPacket(Object object, Object newValue) {
        try {
            final Field field = object.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(object, newValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // TODO: 1/22/23 Throw nice error
            throw new RuntimeException(e);
        }
    }
}
