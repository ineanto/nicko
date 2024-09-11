package xyz.ineanto.nicko.mapping.v1_20;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.biome.BiomeManager;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.mapping.Mapping;

import java.util.List;
import java.util.Set;

public class Mapping1_20 extends Mapping {
    @Override
    public void respawn(Player player) {
        final ServerPlayer entityPlayer = (ServerPlayer) player;

        final ServerLevel world = entityPlayer.serverLevel();
        final ServerPlayerGameMode gameMode = entityPlayer.gameMode;

        // Really Mojang...? (also applies to Bukkit/Spigot maintainers)
        // I'll have to do this everytime I want to update Nicko for the foreseeable future.
        // (until ProtocolLib has reworked its API to be more maintainable that said)

        // I already when through this hassle with NickReloaded back in 2017,
        // when mappings were not included by default, mind you.

        // I had to rework the entire project structure and build process just for... you.

        // I can't be bothered with fighting your game anymore.
        // We need an easy and reliable way to send packets across multiple server versions.
        // And I know that this is easier said than done, the game protocol needs
        // to evolve and be updated, I get it. But I think you can at least try.

        // You made a step forward by providing the mappings for Java, this I can agree with.
        // (and still stripped them from Bedrock against community feedback, haha f*ck you.)
        // However, we still need a stable and reliable Packet API (and so much more!) one day.

        final ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(
                world.dimensionTypeId(),
                world.dimension(),
                BiomeManager.obfuscateSeed(world.getSeed()),
                gameMode.getGameModeForPlayer(),
                gameMode.getPreviousGameModeForPlayer(),
                world.isDebug(),
                world.isFlat(),
                ClientboundRespawnPacket.KEEP_ALL_DATA,
                entityPlayer.getLastDeathLocation(),
                entityPlayer.getPortalCooldown()
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

        // Resend their effects
        for (MobEffectInstance effect : entityPlayer.getActiveEffects()) {
            entityPlayer.connection.send(new ClientboundUpdateMobEffectPacket(entityPlayer.getId(), effect));
        }
    }

    @Override
    public Set<String> supportedVersions() {
        return Set.of("1.20", "1.20.1");
    }
}
