package net.artelnatif.nicko.disguise;

import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.artelnatif.nicko.storage.PlayerDataStore;
import net.artelnatif.nicko.storage.name.PlayerNameStore;
import net.artelnatif.nicko.wrapper.WrapperPlayServerRespawn;
import net.artelnatif.nicko.wrapper.WrapperPlayerServerPlayerInfo;
import net.artelnatif.nicko.wrapper.WrapperPlayerServerPlayerInfoRemove;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
        final ActionResult<Void> actionResult = resetPlayer();
        this.profile.setSkin(null);
        this.profile.setName(null);
        return actionResult;
    }

    public ActionResult<Void> resetPlayer() {
        // TODO: 4/3/23 Reset player
        return new ActionResult<>();
    }

    public ActionResult<Void> updatePlayer(boolean skinChange) {
        final String displayName = profile.getName() == null ? player.getName() : profile.getName();

        Bukkit.broadcastMessage("Building UserProfile");
        final WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(player).withName(displayName);
        final ActionResult<Void> result = updateGameProfileSkin(gameProfile, skinChange);
        if (!result.isError()) {
            updateTabList(gameProfile, displayName);
            respawnPlayer();
        }
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
                        Bukkit.broadcastMessage("Modified properties");
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

    private void respawnPlayer() {
        Bukkit.broadcastMessage("Respawning player");
        final World world = player.getWorld();
        final WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn();
        respawn.setGameMode(player.getGameMode());
        respawn.setDifficulty(world.getDifficulty());
        respawn.setDimension(world);
        respawn.setSeed(world.getSeed());
        respawn.sendPacket(player);
    }

    private void updateTabList(WrappedGameProfile gameProfile, String displayName) {
        Bukkit.broadcastMessage("Updating tablist");
        final WrapperPlayerServerPlayerInfoRemove remove = new WrapperPlayerServerPlayerInfoRemove();
        remove.setUUIDs(ImmutableList.of(player.getUniqueId()));

        final WrapperPlayerServerPlayerInfo update = new WrapperPlayerServerPlayerInfo();
        update.setActions(EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
                EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME,
                EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE,
                EnumWrappers.PlayerInfoAction.UPDATE_LATENCY));
        update.setData(ImmutableList.of(new PlayerInfoData(
                gameProfile,
                player.getPing(),
                EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                WrappedChatComponent.fromText(displayName)
        )));
        remove.sendPacket(player);
        update.sendPacket(player);
    }
}
