package net.artelnatif.nicko.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Optional;

public class v1_19_R1 implements Internals {
    @Override
    public void updateSelf(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final ResourceKey<World> levelResourceKey = entityPlayer.x().ab();
        final PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(entityPlayer.x().Z(),
                levelResourceKey, entityPlayer.s.getWorld().getSeed(),
                entityPlayer.d.b(), entityPlayer.d.c(),
                false,
                false,
                false,
                Optional.empty());

        final boolean wasFlying = player.isFlying();
        entityPlayer.b.a(respawn);
        player.setFlying(wasFlying);
        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.updateInventory();
    }

    @Override
    public void updateOthers(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityPlayer.getBukkitEntity().getEntityId());
        final PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);

        final DataWatcher dataWatcher = entityPlayer.ai();
        final DataWatcherObject<Byte> displayedSkinPartDataWatcher = new DataWatcherObject<>(17, DataWatcherRegistry.a);
        dataWatcher.b(displayedSkinPartDataWatcher, (byte) 0x7f);
        final PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityPlayer.getBukkitEntity().getEntityId(), dataWatcher, true);

        Bukkit.getOnlinePlayers().forEach(online -> {
            EntityPlayer onlineEntityPlayer = ((CraftPlayer) online).getHandle();
            if (onlineEntityPlayer.getBukkitEntity().getUniqueId() != player.getUniqueId()) {
                onlineEntityPlayer.b.a(destroy);
                onlineEntityPlayer.b.a(spawn);
            }
            onlineEntityPlayer.b.a(entityMetadata);
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

        final PacketPlayOutPlayerInfo add = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a);
        final PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, entityPlayer);
        // "It's a Surprise Tool That Will Help Us Later!"
        final ProfilePublicKey.a key = remove.b().get(0).e();

        add.b().clear();
        add.b().add(new PacketPlayOutPlayerInfo.PlayerInfoData(gameProfile,
                player.getPing(),
                EnumGamemode.a(player.getGameMode().ordinal()),
                IChatBaseComponent.a(profileName),
                key)); // f mojang

        Bukkit.getOnlinePlayers().forEach(online -> {
            EntityPlayer onlineEntityPlayer = ((CraftPlayer) online).getHandle();
            onlineEntityPlayer.b.a(remove);
            onlineEntityPlayer.b.a(add);
        });
        updateOthers(player);
        return new ActionResult<>();
    }
}
