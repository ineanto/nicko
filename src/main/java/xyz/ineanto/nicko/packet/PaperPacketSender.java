//package xyz.ineanto.nicko.packet;
//
//import com.destroystokyo.paper.profile.CraftPlayerProfile;
//import com.destroystokyo.paper.profile.PlayerProfile;
//import it.unimi.dsi.fastutil.ints.IntList;
//import net.minecraft.Optionull;
//import net.minecraft.network.chat.MutableComponent;
//import net.minecraft.network.chat.RemoteChatSession;
//import net.minecraft.network.chat.contents.PlainTextContents;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.*;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.phys.Vec3;
//import org.bukkit.Bukkit;
//import org.bukkit.craftbukkit.entity.CraftPlayer;
//import org.bukkit.entity.Player;
//import xyz.ineanto.nicko.Nicko;
//import xyz.ineanto.nicko.appearance.ActionResult;
//import xyz.ineanto.nicko.language.LanguageKey;
//import xyz.ineanto.nicko.mojang.MojangAPI;
//import xyz.ineanto.nicko.mojang.MojangSkin;
//import xyz.ineanto.nicko.profile.NickoProfile;
//
//import java.io.IOException;
//import java.util.EnumSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Random;
//import java.util.concurrent.ExecutionException;
//
///**
// * Look at this Mojang.
// * I want you to really stare at this code.
// * You made me do this.
// */
//public class PaperPacketSender implements PacketSender {
//    private final Player player;
//    private final NickoProfile profile;
//
//    public PaperPacketSender(Player player, NickoProfile profile) {
//        this.player = player;
//        this.profile = profile;
//    }
//
//    @Override
//    public void sendEntityRespawn() {
//        if (!profile.hasData()) return;
//
//        final ClientboundRemoveEntitiesPacket destroy = new ClientboundRemoveEntitiesPacket(IntList.of(player.getEntityId()));
//        final ClientboundAddEntityPacket add = new ClientboundAddEntityPacket(
//                new Random().nextInt(9999),
//                player.getUniqueId(),
//                player.getX(),
//                player.getY(),
//                player.getZ(),
//                player.getPitch(),
//                player.getYaw(),
//                EntityType.PLAYER,
//                0,
//                Vec3.ZERO,
//                player.getBodyYaw()
//        );
//
//        Bukkit.getOnlinePlayers().stream().filter(receiver -> receiver.getUniqueId() != player.getUniqueId()).forEach(receiver -> {
//            sendPacket(destroy, player);
//            sendPacket(add, player);
//        });
//    }
//
//    @Override
//    public ActionResult updatePlayerProfile(String name) {
//        final PlayerProfile playerProfile = new CraftPlayerProfile(player.getUniqueId(), name);
//        // Copy previous properties to preserve skin
//        playerProfile.setProperties(playerProfile.getProperties());
//        player.setPlayerProfile(playerProfile);
//        return ActionResult.ok();
//    }
//
//    @Override
//    public ActionResult updatePlayerProfileProperties() {
//        final PlayerProfile playerProfile = new CraftPlayerProfile(player.getUniqueId(), profile.getName() == null ? player.getName() : profile.getName());
//
//        try {
//            final MojangAPI mojangAPI = Nicko.getInstance().getMojangAPI();
//
//            final Optional<String> uuid = mojangAPI.getUUID(profile.getSkin());
//            if (uuid.isEmpty()) {
//                return ActionResult.error(LanguageKey.Error.MOJANG);
//            }
//
//            final Optional<MojangSkin> skin = mojangAPI.getSkin(uuid.get());
//            if (skin.isEmpty()) {
//                return ActionResult.error(LanguageKey.Error.MOJANG);
//            }
//
//            final MojangSkin skinResult = skin.get();
//            playerProfile.setProperties(skinResult.asProfileProperties());
//            player.setPlayerProfile(playerProfile);
//            return ActionResult.ok();
//        } catch (ExecutionException | IOException e) {
//            return ActionResult.error(LanguageKey.Error.CACHE);
//        }
//    }
//
//    @Override
//    public void sendEntityMetadataUpdate() {
//        final SynchedEntityData.DataValue<?> dataValueComponent =
//                new SynchedEntityData.DataItem<>(
//                        new EntityDataAccessor<>(17, EntityDataSerializers.BYTE),
//                        (byte) 0x7f
//                ).value();
//
//        final ClientboundSetEntityDataPacket data = new ClientboundSetEntityDataPacket(player.getEntityId(), List.of(dataValueComponent));
//        sendPacket(data, player);
//    }
//
//    @Override
//    public void sendPlayerRespawn() {
//        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
//        final ServerLevel level = serverPlayer.serverLevel();
//
//        final ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(serverPlayer.createCommonSpawnInfo(level), (byte) 0x03);
//        sendPacket(respawn, player);
//    }
//
//    @Override
//    public void sendTabListUpdate(String displayName) {
//        final ServerPlayer serverPlayer = (((CraftPlayer) player)).getHandle();
//
//        final EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.of(
//                ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
//                ClientboundPlayerInfoUpdatePacket.Action.INITIALIZE_CHAT,
//                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED,
//                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
//                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_GAME_MODE,
//                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY);
//
//        final List<ClientboundPlayerInfoUpdatePacket.Entry> entries = List.of(new ClientboundPlayerInfoUpdatePacket.Entry(
//                serverPlayer.getUUID(),
//                serverPlayer.gameProfile,
//                true,
//                serverPlayer.connection.latency(),
//                serverPlayer.gameMode.getGameModeForPlayer(),
//                MutableComponent.create(new PlainTextContents.LiteralContents(displayName)),
//                true,
//                serverPlayer.getTabListOrder(),
//                Optionull.map(serverPlayer.getChatSession(), RemoteChatSession::asData)
//        ));
//
//        final ClientboundPlayerInfoUpdatePacket update = new ClientboundPlayerInfoUpdatePacket(actions, entries);
//        final ClientboundPlayerInfoRemovePacket remove = new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId()));
//
//        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
//            sendPacket(remove, onlinePlayer);
//            sendPacket(update, onlinePlayer);
//        });
//    }
//
//    private void sendPacket(Packet<?> packet, Player player) {
//        (((CraftPlayer) player).getHandle()).connection.send(packet);
//    }
//}