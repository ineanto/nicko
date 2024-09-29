package xyz.ineanto.nicko.debug;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.MinecraftKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import xyz.ineanto.nicko.Nicko;

import java.util.Arrays;
import java.util.Optional;

public class RespawnPacketListener implements PacketListener {
    @Override
    public void onPacketSending(PacketEvent event) {
        try {
            final Optional<World> world = getWorld(event);
            if (world.isEmpty()) {
                Bukkit.broadcast(Component.text("did not find the world the player was in"));
                return;
            }

            Bukkit.broadcast(Component.text("found " + world.get().getName() + "!"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {

    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
        return ListeningWhitelist.newBuilder().types(PacketType.Play.Server.RESPAWN).build();
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
        return ListeningWhitelist.EMPTY_WHITELIST;
    }

    @Override
    public Plugin getPlugin() {
        return Nicko.getInstance();
    }

    private boolean keysEquals(MinecraftKey wrappedKey, NamespacedKey bukkitKey) {
        // compare bukkit minecraft key and NMS wrapped minecraft key
        return wrappedKey.getPrefix().equals(bukkitKey.getNamespace()) && wrappedKey.getKey().equals(bukkitKey.getKey());
    }

    public Optional<World> getWorld(PacketEvent event) throws Throwable {
        final Class<?> commonPlayerInfoClazz = MinecraftReflection.getMinecraftClass("network.protocol.game.CommonPlayerSpawnInfo");
        // access CommonPlayerSpawnInfo, first field of that type in the Respawn / Login packets
        final Object packetHandle = event.getPacket().getHandle();
        final Object commonSpawnData = packetHandle.getClass().getRecordComponents()[0].getAccessor().invoke(packetHandle);

        Arrays.stream(commonSpawnData.getClass().getRecordComponents()).forEach(component -> {
                    component.getAccessor().setAccessible(true);
                    System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                    System.out.println(component.getName());
                    System.out.println(component.getType().getSimpleName());
                    System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-");
                    component.getAccessor().setAccessible(false);
                }
        );

        // get the key of the level the player is joining. Second field in the object. First of type ResourceKey
        /**MinecraftKey key = MinecraftKey.fromHandle(Accessors.getFieldAccessor(commonSpawnData.getClass(), MinecraftReflection.getResourceKey(), true)
         .get(commonSpawnData)); // wrap to ProtocolLib handle
         for (World world : Bukkit.getWorlds()) {
         if (keysEquals(key, world.getKey())) {
         return Optional.of(world);
         }
         }*/
        return Optional.empty();
    }
}
