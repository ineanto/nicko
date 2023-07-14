package xyz.atnrch.nicko.profile;

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
import xyz.atnrch.nicko.i18n.Locale;
import xyz.atnrch.nicko.mojang.MojangAPI;
import xyz.atnrch.nicko.mojang.MojangSkin;
import xyz.atnrch.nicko.storage.PlayerDataStore;
import xyz.atnrch.nicko.storage.name.PlayerNameStore;
import xyz.atnrch.nicko.wrapper.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ProfileManager {
    private final NickoProfile profile;
    private final Player player;
    private final UUID uuid;
    private final NickoBukkit instance = NickoBukkit.getInstance();
    private final PlayerDataStore dataStore = instance.getDataStore();
    private final PlayerNameStore nameStore = instance.getNameStore();

    private ProfileManager(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.uuid = uuid;
        this.profile = dataStore.getData(uuid).orElse(NickoProfile.EMPTY_PROFILE.clone());
    }

    private ProfileManager(String name) {
        this.player = null;
        this.uuid = null;
        this.profile = dataStore.getOfflineData(name).orElse(NickoProfile.EMPTY_PROFILE.clone());
    }

    public static ProfileManager get(Player player) {
        return new ProfileManager(player.getUniqueId());
    }

    public static ProfileManager get(String name) {
        return new ProfileManager(name);
    }

    public boolean hasData() {
        return !profile.isEmpty();
    }

    public void setSkin(String skin) {
        profile.setSkin(skin);
        dataStore.getCache().cache(uuid, profile);
    }

    public String getSkin() {
        return profile.getSkin();
    }

    public boolean needsASkinChange() {
        return profile.getSkin() != null && !profile.getSkin().equals(player.getName());
    }

    public void setName(String name) {
        profile.setName(name);
        dataStore.getCache().cache(uuid, profile);
    }

    public String getName() {
        return profile.getName();
    }

    public void setLocale(Locale locale) {
        profile.setLocale(locale);
        dataStore.getCache().cache(uuid, profile);
    }

    public Locale getLocale() {
        return profile.getLocale();
    }

    public NickoProfile getProfile() {
        return profile;
    }

    public void setNameAndSkin(String name, String skin) {
        this.profile.setName(name);
        this.profile.setSkin(skin);
        dataStore.getCache().cache(uuid, profile);
    }

    public ActionResult reset() {
        final String defaultName = nameStore.getStoredName(player);
        this.profile.setName(defaultName);
        this.profile.setSkin(defaultName);
        final ActionResult actionResult = updatePlayer(true, true);
        if (!actionResult.isError()) {
            this.profile.setSkin(null);
            this.profile.setName(null);
        }
        return actionResult;
    }

    public ActionResult updatePlayer(boolean skinChange, boolean reset) {
        final String displayName = profile.getName() == null ? player.getName() : profile.getName();
        final WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(player).withName(displayName);
        final ActionResult result = updateGameProfileSkin(gameProfile, skinChange, reset);
        if (!result.isError()) {
            updateMetadata();
            updateTabList(gameProfile, displayName);
            respawnPlayer();
            updateOthers();
        }
        return result;
    }

    public void updateOthers() {
        final WrapperPlayServerEntityDestroy destroy = new WrapperPlayServerEntityDestroy();
        final WrapperPlayServerNamedEntitySpawn spawn = new WrapperPlayServerNamedEntitySpawn();
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
        final boolean changeOnlyName = profile.getSkin() != null && !profile.getSkin().equalsIgnoreCase(player.getName());

        if (skinChange || changeOnlyName) {
            Optional<MojangSkin> skin;
            try {
                final MojangAPI mojang = NickoBukkit.getInstance().getMojangAPI();
                final Optional<String> uuid = mojang.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = reset ? mojang.getSkinWithoutCaching(uuid.get()) : mojang.getSkin(uuid.get());
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
        final WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn();
        respawn.setDimension(world);
        respawn.setSeed(world.getSeed());
        respawn.setGameMode(player.getGameMode());
        respawn.setPreviousGameMode(player.getGameMode());
        respawn.setDifficulty(world.getDifficulty());
        respawn.setCopyMetadata(true);
        respawn.sendPacket(player);
        player.setFlying(wasFlying);
        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.updateInventory();
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

        // Yes, I skip providing chat session data.
        // Yes, this will cause players to get kicked
        // as soon as they send a message on versions above 1.19.2.
        // No, I'll not waste another day fixing their mess.
        // Go cry about it to Mojang.
        // (Long live NoEncryption!)
        add.setData(ImmutableList.of(new PlayerInfoData(
                player.getUniqueId(),
                player.getPing(),
                true,
                EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                gameProfile,
                WrappedChatComponent.fromText(displayName)
        )));
        add.broadcastPacket();
    }
}