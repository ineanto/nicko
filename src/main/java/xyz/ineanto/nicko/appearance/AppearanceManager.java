package xyz.ineanto.nicko.appearance;

import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.IntList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.ineanto.nicko.storage.name.PlayerNameStore;
import xyz.ineanto.nicko.wrapper.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AppearanceManager {
    private final Nicko instance = Nicko.getInstance();
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

        final ActionResult result = updatePlayer(true, true);
        if (!result.isError()) {
            profile.setName(null);
            profile.setSkin(null);
            dataStore.getCache().cache(player.getUniqueId(), profile);
        }
        return result;
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
            respawnEntityForOthers();
        }
        return result;
    }

    public ActionResult updateForOthers(boolean skinChange, boolean reset) {
        final NickoProfile profile = getNickoProfile();
        final String displayName = profile.getName() == null ? player.getName() : profile.getName();
        final WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(player).withName(displayName);
        final ActionResult result = updateGameProfileSkin(gameProfile, skinChange, reset);
        if (!result.isError()) {
            updateMetadata();
            updateTabList(gameProfile, displayName);
            respawnEntityForOthers();
        }
        return result;
    }

    private NickoProfile getNickoProfile() {
        final Optional<NickoProfile> optionalProfile = dataStore.getData(player.getUniqueId());
        return optionalProfile.orElse(NickoProfile.EMPTY_PROFILE.clone());
    }

    public void respawnEntityForOthers() {
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
                final MojangAPI mojangAPI = Nicko.getInstance().getMojangAPI();
                final Optional<String> uuid = mojangAPI.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = reset ? mojangAPI.getSkinWithoutCaching(uuid.get()) : mojangAPI.getSkin(uuid.get());
                    if (skin.isPresent()) {
                        final MojangSkin skinResult = skin.get();
                        final Multimap<String, WrappedSignedProperty> properties = gameProfile.getProperties();
                        properties.get("textures").clear();
                        properties.put("textures", new WrappedSignedProperty("textures", skinResult.value(), skinResult.signature()));
                    } else {
                        reset();
                        return ActionResult.error(LanguageKey.Error.MOJANG_SKIN);
                    }
                } else {
                    reset();
                    return ActionResult.error(LanguageKey.Error.MOJANG_NAME);
                }
                return ActionResult.ok();
            } catch (ExecutionException e) {
                return ActionResult.error(LanguageKey.Error.CACHE);
            } catch (IOException e) {
                reset();
                return ActionResult.error(LanguageKey.Error.MOJANG_NAME);
            } catch (InterruptedException e) {
                return ActionResult.error("Unknown error");
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
        final int foodLevel = player.getFoodLevel();

        final WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn();
        //respawn.setDimension(world);
        respawn.setSeed(world.getSeed());
        respawn.setGameMode(player.getGameMode());
        respawn.setPreviousGameMode(player.getGameMode());
        respawn.setCopyMetadata(true);
        //respawn.sendPacket(player);

        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.setAllowFlight(wasAllowedToFly);
        player.setFlying(wasFlying);
        player.updateInventory();
        player.sendHealthUpdate();
        player.setFoodLevel(foodLevel);
    }

    private void updateTabList(WrappedGameProfile gameProfile, String displayName) {
        final WrapperPlayerServerPlayerInfo add = new WrapperPlayerServerPlayerInfo();
        final WrapperPlayerServerPlayerInfoRemove remove = new WrapperPlayerServerPlayerInfoRemove();
        final EnumSet<EnumWrappers.PlayerInfoAction> actions = EnumSet.of(
                EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.INITIALIZE_CHAT,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
                EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME,
                EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE,
                EnumWrappers.PlayerInfoAction.UPDATE_LATENCY);
        remove.setUUIDs(List.of(player.getUniqueId()));
        remove.broadcastPacket();
        add.setActions(actions);

        add.setData(List.of(new PlayerInfoData(
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