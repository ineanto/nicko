package net.artelnatif.nicko.impl;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.disguise.UpdateResult;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class v1_18_R2 implements Internals {
    @Override
    public void updateSelf(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final ResourceKey<World> levelResourceKey = entityPlayer.x().aa();
        final CraftWorld world = entityPlayer.s.getWorld();
        // wtf is that ?
        final PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(Holder.a(entityPlayer.s.q_()),
                levelResourceKey, world.getSeed(),
                entityPlayer.d.b(), entityPlayer.d.c(),
                false,
                false,
                false);

        final boolean wasFlying = player.isFlying();
        final ItemStack itemOnCursor = player.getItemOnCursor();
        entityPlayer.b.a(respawn);
        player.setFlying(wasFlying);
        player.setItemOnCursor(itemOnCursor);
        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.updateInventory();
    }

    @Override
    public void updateOthers(Player player) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityPlayer.getBukkitEntity().getEntityId());
        final PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);

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
        final DataWatcher dataWatcher = entityPlayer.ai();
        final DataWatcherObject<Byte> displayedSkinPartDataWatcher = new DataWatcherObject<>(17, DataWatcherRegistry.a);
        dataWatcher.b(displayedSkinPartDataWatcher, (byte) 0x7f); // 127, all masks combined
        final PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityPlayer.getBukkitEntity().getEntityId(), dataWatcher, true);

        Bukkit.getOnlinePlayers().forEach(online -> {
            EntityPlayer onlineEntityPlayer = ((CraftPlayer) online).getHandle();
            if (onlineEntityPlayer.getBukkitEntity().getUniqueId() != player.getUniqueId()) {
                onlineEntityPlayer.b.a(destroy);
                onlineEntityPlayer.b.a(spawn);
                onlineEntityPlayer.b.a(entityMetadata);
            }
        });
    }

    @Override
    public UpdateResult updateProfile(Player player, NickoProfile profile, boolean skinChange) {
        final CraftPlayer craftPlayer = (CraftPlayer) player;
        final EntityPlayer entityPlayer = craftPlayer.getHandle();
        Optional<MojangSkin> skin;

        final PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, entityPlayer);
        entityPlayer.b.a(remove);

        final PacketPlayOutPlayerInfo add = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a);
        final GameProfile gameProfile = new GameProfile(player.getUniqueId(), profile.getName());

        if (skinChange) {
            try {
                final Optional<String> uuid = NickoBukkit.getInstance().getMojangAPI().getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = NickoBukkit.getInstance().getMojangAPI().getSkin(uuid.get());
                    if (skin.isPresent()) {
                        final PropertyMap properties = gameProfile.getProperties();
                        properties.put("textures", new Property("textures", skin.get().value(), skin.get().signature()));
                        updateSelf(player);
                    } else {
                        return new UpdateResult(I18NDict.Error.SKIN_FAIL_MOJANG);
                    }
                } else {
                    return new UpdateResult(I18NDict.Error.NAME_FAIL_MOJANG);
                }
            } catch (ExecutionException e) {
                return new UpdateResult(I18NDict.Error.SKIN_FAIL_CACHE);
            } catch (IOException e) {
                return new UpdateResult(I18NDict.Error.UNEXPECTED_ERROR);
            }
        }

        add.b().clear();
        add.b().add(new PacketPlayOutPlayerInfo.PlayerInfoData(gameProfile,
                player.getPing(),
                EnumGamemode.a(player.getGameMode().ordinal()), IChatBaseComponent.a(profile.getName())));
        entityPlayer.b.a(add);

        Bukkit.getOnlinePlayers().forEach(online -> {
            EntityPlayer onlineEntityPlayer = ((CraftPlayer) online).getHandle();
            onlineEntityPlayer.b.a(remove);
            onlineEntityPlayer.b.a(add);
        });
        updateOthers(player);
        return new UpdateResult();
    }
}
