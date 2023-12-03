package xyz.atnrch.nicko.appearance;

import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.mojang.MojangAPI;
import xyz.atnrch.nicko.mojang.MojangSkin;
import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.atnrch.nicko.storage.PlayerDataStore;
import xyz.atnrch.nicko.storage.name.PlayerNameStore;
import xyz.atnrch.nicko.wrapper.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AppearanceManager {
    private final NickoBukkit instance = NickoBukkit.getInstance();
    private final PlayerDataStore dataStore = instance.getDataStore();
    private final PlayerNameStore nameStore = instance.getNameStore();

    private final Player player;

    public AppearanceManager(Player player) {
        this.player = player;
    }

    public ActionResult reset() {
        final NickoProfile profile = getNickoProfile();
        final String defaultName = nameStore.getStoredName(player);
        profile.setName(defaultName);
        profile.setSkin(defaultName);
        dataStore.getCache().cache(player.getUniqueId(), profile);
        final ActionResult actionResult = updatePlayer(true, true);
        if (!actionResult.isError()) {
            profile.setSkin(null);
            profile.setName(null);
            dataStore.getCache().cache(player.getUniqueId(), profile);
        }
        return actionResult;
    }

    public ActionResult updatePlayer(boolean skinChange, boolean reset) {
        final NickoProfile profile = getNickoProfile();
        final String displayName = profile.getName() == null ? player.getName() : profile.getName();
        final WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(player).withName(displayName);
        final ActionResult result = updateGameProfileSkin(gameProfile, skinChange, reset);
        if (!result.isError()) {
            updateMetadata();
            updateTabList(gameProfile, displayName);
            respawnPlayer();
            updateForOthers();
        }
        return result;
    }

    private NickoProfile getNickoProfile() {
        final Optional<NickoProfile> optionalProfile = dataStore.getData(player.getUniqueId());
        return optionalProfile.orElse(NickoProfile.EMPTY_PROFILE.clone());
    }

    public void updateForOthers() {
        final NickoProfile nickoProfile = getNickoProfile();
        if (!nickoProfile.hasData()) return;

        final WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
        final WrapperPlayServerSpawnEntity spawn = new WrapperPlayServerSpawnEntity();
        destroy.setEntityIds(IntList.of(player.getEntityId()));
        spawn.setEntityId(player.getEntityId());
        spawn.setLocation(player.getLocation());
        spawn.setPlayerId(player.getUniqueId());
        Bukkit.getOnlinePlayers().stream().filter(receiver -> receiver.getUniqueId() != player.getUniqueId()).forEach(receiver -> {
            destroy.sendPacket(receiver);
            spawn.sendPacket(receiver);
        });
    }


    private ActionResult updateGameProfileSkin(WrappedGameProfile gameProfile, boolean skinChange, boolean reset) {
        final NickoProfile profile = getNickoProfile();

        if (skinChange) {
            Optional<MojangSkin> skin;
            try {
                final MojangAPI mojangAPI = NickoBukkit.getInstance().getMojangAPI();
                final Optional<String> uuid = mojangAPI.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = reset ? mojangAPI.getSkinWithoutCaching(uuid.get()) : mojangAPI.getSkin(uuid.get());
                    if (skin.isPresent()) {
                        final MojangSkin skinResult = skin.get();
                        final Multimap<String, WrappedSignedProperty> properties = gameProfile.getProperties();
                        properties.get("textures").clear();
                        properties.put("textures", new WrappedSignedProperty("textures", skinResult.getValue(), skinResult.getSignature()));
                    } else {
                        return ActionResult.error(I18NDict.Error.MOJANG_SKIN);
                    }
                } else {
                    return ActionResult.error(I18NDict.Error.MOJANG_NAME);
                }
                return ActionResult.ok();
            } catch (ExecutionException e) {
                return ActionResult.error(I18NDict.Error.CACHE);
            } catch (IOException e) {
                return ActionResult.error(I18NDict.Error.MOJANG_NAME);
            }
        }
        return ActionResult.ok();
    }

    private void updateMetadata() {
        final WrappedDataWatcher entityWatcher = WrappedDataWatcher.getEntityWatcher(player);
        entityWatcher.setObject(17, (byte) 0x7f, true);
    }

    private void respawnPlayer() {
        final World world = player.getWorld();
        final boolean wasFlying = player.isFlying();
        final boolean wasAllowedToFly = player.getAllowFlight();
        final WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn();
        respawn.setDimension(world);
        respawn.setSeed(world.getSeed());
        respawn.setGameMode(player.getGameMode());
        respawn.setPreviousGameMode(player.getGameMode());
        respawn.setDifficulty(world.getDifficulty());
        respawn.setCopyMetadata(true);
        respawn.sendPacket(player);
        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.setAllowFlight(wasAllowedToFly);
        player.setFlying(wasFlying);
        player.updateInventory(); // Marked as unstable.
    }

    @SuppressWarnings("deprecation")
    private void updateTabList(WrappedGameProfile gameProfile, String displayName) {
        final WrapperPlayerServerPlayerInfo add = new WrapperPlayerServerPlayerInfo();
        if (MinecraftVersion.FEATURE_PREVIEW_UPDATE.atOrAbove()) {
            final WrapperPlayerServerPlayerInfoRemove remove = new WrapperPlayerServerPlayerInfoRemove();
            final EnumSet<EnumWrappers.PlayerInfoAction> actions = EnumSet.of(
                    EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                    EnumWrappers.PlayerInfoAction.INITIALIZE_CHAT,
                    EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
                    EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME,
                    EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE,
                    EnumWrappers.PlayerInfoAction.UPDATE_LATENCY);
            remove.setUUIDs(ImmutableList.of(player.getUniqueId()));
            remove.broadcastPacket();
            add.setActions(actions);
        } else {
            final WrapperPlayerServerPlayerInfo remove = new WrapperPlayerServerPlayerInfo();
            remove.setActions(EnumSet.of(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER));
            add.setActions(EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));
            remove.broadcastPacket();
        }

        add.setData(ImmutableList.of(new PlayerInfoData(
                player.getUniqueId(),
                player.getPing(),
                true,
                EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                gameProfile,
                WrappedChatComponent.fromText(displayName),
                WrappedRemoteChatSessionData.fromPlayer(player)
        )));
        add.broadcastPacket();
    }
}