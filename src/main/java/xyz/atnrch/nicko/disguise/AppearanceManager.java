package xyz.atnrch.nicko.disguise;

import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.mojang.MojangAPI;
import xyz.atnrch.nicko.mojang.MojangSkin;
import xyz.atnrch.nicko.storage.PlayerDataStore;
import xyz.atnrch.nicko.storage.name.PlayerNameStore;
import xyz.atnrch.nicko.wrapper.WrapperPlayServerRespawn;
import xyz.atnrch.nicko.wrapper.WrapperPlayerServerPlayerInfo;
import xyz.atnrch.nicko.wrapper.WrapperPlayerServerPlayerInfoRemove;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AppearanceManager {
    private final NickoProfile profile;
    private final Player player;
    private final NickoBukkit instance = NickoBukkit.getInstance();
    private final PlayerDataStore dataStore = instance.getDataStore();
    private final PlayerNameStore nameStore = instance.getNameStore();

    private AppearanceManager(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.profile = dataStore.getData(uuid).orElse(NickoProfile.EMPTY_PROFILE.clone());
    }

    private AppearanceManager(String name) {
        this.player = null;
        this.profile = dataStore.getOfflineData(name).orElse(NickoProfile.EMPTY_PROFILE.clone());
    }

    public static AppearanceManager get(Player player) {
        return new AppearanceManager(player.getUniqueId());
    }

    public static AppearanceManager get(String name) {
        return new AppearanceManager(name);
    }

    public boolean hasData() {
        return !profile.isEmpty();
    }

    public void setSkin(String skin) {
        profile.setSkin(skin);
    }

    public String getSkin() {
        return profile.getSkin();
    }

    public boolean needsASkinChange() {
        return profile.getSkin() != null && !profile.getSkin().equals(player.getName());
    }

    public void setName(String name) {
        profile.setName(name);
    }

    public String getName() {
        return profile.getName();
    }

    public NickoProfile getProfile() {
        return profile;
    }

    public void setNameAndSkin(String name, String skin) {
        this.profile.setName(name);
        this.profile.setSkin(skin);
        updatePlayer(true);
    }

    public ActionResult<Void> reset() {
        final String defaultName = nameStore.getStoredName(player);
        this.profile.setName(defaultName);
        this.profile.setSkin(defaultName);
        final ActionResult<Void> actionResult = updatePlayer(true);
        if (!actionResult.isError()) {
            this.profile.setSkin(null);
            this.profile.setName(null);
        }
        return actionResult;
    }

    public ActionResult<Void> updatePlayer(boolean skinChange) {
        final String displayName = profile.getName() == null ? player.getName() : profile.getName();

        final WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(player).withName(displayName);
        final ActionResult<Void> result = updateGameProfileSkin(gameProfile, skinChange);
        if (!result.isError()) {
            updateMetadata();
            updateTabList(gameProfile, displayName);
            respawnPlayer();
        }
        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        return new ActionResult<>();
    }


    private ActionResult<Void> updateGameProfileSkin(WrappedGameProfile gameProfile, boolean skinChange) {
        final boolean changeOnlyName = profile.getSkin() != null && !profile.getSkin().equalsIgnoreCase(player.getName());

        if (skinChange || changeOnlyName) {
            Optional<MojangSkin> skin;
            try {
                final MojangAPI mojang = NickoBukkit.getInstance().getMojangAPI();
                final Optional<String> uuid = mojang.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = mojang.getSkin(uuid.get());
                    if (skin.isPresent()) {
                        final MojangSkin skinResult = skin.get();
                        final Multimap<String, WrappedSignedProperty> properties = gameProfile.getProperties();
                        properties.get("textures").clear();
                        properties.put("textures", new WrappedSignedProperty("textures", skinResult.getValue(), skinResult.getSignature()));
                    }
                }
                return new ActionResult<>();
            } catch (ExecutionException e) {
                return new ActionResult<>(I18NDict.Error.SKIN_FAIL_CACHE);
            } catch (IOException e) {
                return new ActionResult<>(I18NDict.Error.NAME_FAIL_MOJANG);
            }
        }
        return new ActionResult<>();
    }

    private void updateMetadata() {
        final WrappedDataWatcher entityWatcher = WrappedDataWatcher.getEntityWatcher(player);
        entityWatcher.setObject(17, (byte) 0x7f, true);
    }

    private void respawnPlayer() {
        final World world = player.getWorld();
        final WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn();
        respawn.setDimension(world);
        respawn.setSeed(world.getSeed());
        respawn.setGameMode(player.getGameMode());
        respawn.setPreviousGameMode(player.getGameMode());
        respawn.setDifficulty(world.getDifficulty());
        respawn.setCopyMetadata(false);
        respawn.getHandle().getBooleans().write(0, false); // is debug
        respawn.getHandle().getBooleans().write(0, false); // is flat
        respawn.sendPacket(player);
    }

    private void updateTabList(WrappedGameProfile gameProfile, String displayName) {
        final WrapperPlayerServerPlayerInfoRemove remove = new WrapperPlayerServerPlayerInfoRemove();
        final WrapperPlayerServerPlayerInfo update = new WrapperPlayerServerPlayerInfo();
        final EnumSet<EnumWrappers.PlayerInfoAction> actions = EnumSet.of(
                EnumWrappers.PlayerInfoAction.REMOVE_PLAYER, // Necessary for 1.19.2 and below
                EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.INITIALIZE_CHAT,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
                EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME,
                EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE,
                EnumWrappers.PlayerInfoAction.UPDATE_LATENCY);
        update.setData(ImmutableList.of(new PlayerInfoData(
                player.getUniqueId(),
                player.getPing(),
                true,
                EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                gameProfile,
                WrappedChatComponent.fromText(displayName)
                // Yes, I skip providing chat session data.
                // Yes, this will cause players to get kicked
                // as soon as they send a message on versions above 1.19.2.
                // No, I'll not waste another day fixing their mess. Go cry about it to Mojang.
                // (Long live NoEncryption!)
        )));
        if (MinecraftVersion.FEATURE_PREVIEW_UPDATE.atOrAbove()) {
            actions.remove(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
            remove.setUUIDs(ImmutableList.of(player.getUniqueId()));
            remove.sendPacket(player);
        }
        update.setActions(actions);
        update.sendPacket(player);
    }
}