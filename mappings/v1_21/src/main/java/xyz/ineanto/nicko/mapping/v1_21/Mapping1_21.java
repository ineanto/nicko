package xyz.ineanto.nicko.mapping.v1_21;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.mapping.Mapping;

import java.util.List;
import java.util.Set;

public class Mapping1_21 extends Mapping {

    @Override
    public void respawn(Player player) {
        final ServerPlayer entityPlayer = (ServerPlayer) player;

        final ServerLevel world = entityPlayer.serverLevel();
        final ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(
                entityPlayer.createCommonSpawnInfo(world),
                ClientboundRespawnPacket.KEEP_ALL_DATA
        );

        entityPlayer.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId())));
        entityPlayer.connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(entityPlayer)));

        entityPlayer.connection.send(respawn);

        entityPlayer.onUpdateAbilities();
        entityPlayer.connection.teleport(player.getLocation());

        entityPlayer.resetSentInfo();

        final PlayerList playerList = entityPlayer.server.getPlayerList();
        playerList.sendPlayerPermissionLevel(entityPlayer);
        playerList.sendLevelInfo(entityPlayer, world);
        playerList.sendAllPlayerInfo(entityPlayer);

        for (MobEffectInstance effect : entityPlayer.getActiveEffects()) {
            entityPlayer.connection.send(new ClientboundUpdateMobEffectPacket(entityPlayer.getId(), effect, true));
        }
    }

    @Override
    public Set<String> supportedVersions() {
        return Set.of("1.21", "1.21.1");
    }
}
