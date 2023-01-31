package net.artelnatif.nicko.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class v1_16_R3 implements Internals {
    @Override
    public void updateSelf(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final ResourceKey<World> levelResourceKey = entityPlayer.getWorld().getDimensionKey();
        final CraftWorld world = entityPlayer.getWorld().getWorld();
        final PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(entityPlayer.getWorld().getDimensionManager(),
                levelResourceKey, world.getSeed(),
                entityPlayer.playerInteractManager.c(), entityPlayer.playerInteractManager.getGameMode(),
                false,
                false,
                false);

        final boolean wasFlying = player.isFlying();
        entityPlayer.playerConnection.sendPacket(respawn);
        player.setFlying(wasFlying);
        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.updateInventory();
    }

    @Override
    public void updateOthers(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityPlayer.getBukkitEntity().getEntityId());
        final PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);

        final DataWatcher dataWatcher = entityPlayer.getDataWatcher();
        final DataWatcherObject<Byte> displayedSkinPartDataWatcher = new DataWatcherObject<>(17, DataWatcherRegistry.a);
        dataWatcher.set(displayedSkinPartDataWatcher, (byte) 0x7f);
        final PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityPlayer.getBukkitEntity().getEntityId(), dataWatcher, true);

        Bukkit.getOnlinePlayers().forEach(online -> {
            EntityPlayer onlineEntityPlayer = ((CraftPlayer) online).getHandle();
            if (onlineEntityPlayer.getBukkitEntity().getUniqueId() != player.getUniqueId()) {
                onlineEntityPlayer.playerConnection.sendPacket(destroy);
                onlineEntityPlayer.playerConnection.sendPacket(spawn);
            }
            onlineEntityPlayer.playerConnection.sendPacket(entityMetadata);
        });
    }

    @Override
    public ActionResult<Void> updateProfile(Player player, NickoProfile profile, boolean skinChange, boolean reset) {
        final boolean changeOnlyName = profile.getSkin() != null && !profile.getSkin().equalsIgnoreCase(player.getName());
        final String profileName = profile.getName() == null ? player.getName() : profile.getName();

        final CraftPlayer craftPlayer = (CraftPlayer) player;
        final EntityPlayer entityPlayer = craftPlayer.getHandle();
        final GameProfile gameProfile = new GameProfile(player.getUniqueId(), profileName);

        if (skinChange || changeOnlyName) {
            final ActionResult<MojangSkin> skinFetch = fetchSkinTextures(profile, reset);
            if (!skinFetch.isError()) {
                final MojangSkin skin = skinFetch.getResult();
                final PropertyMap properties = gameProfile.getProperties();
                properties.removeAll("textures");
                properties.put("textures", new Property("textures", skin.value(), skin.signature()));
                updateSelf(player);
            }
        }

        final PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        final PacketPlayOutPlayerInfo add = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        final IChatBaseComponent name = new ChatComponentText(profileName);

        // what the fuck? didn't even KNOW instantiating a new class from an existing object was possible in java
        final PacketPlayOutPlayerInfo.PlayerInfoData data = remove.new PlayerInfoData(gameProfile,
                player.getPing(),
                EnumGamemode.getById(player.getGameMode().ordinal()), name);
        final ArrayList<PacketPlayOutPlayerInfo.PlayerInfoData> list = new ArrayList<>();
        list.add(data);
        spoofPlayerInfoPacket(add, list);

        Bukkit.getOnlinePlayers().forEach(online -> {
            EntityPlayer onlineEntityPlayer = ((CraftPlayer) online).getHandle();
            onlineEntityPlayer.playerConnection.sendPacket(remove);
            onlineEntityPlayer.playerConnection.sendPacket(add);
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
