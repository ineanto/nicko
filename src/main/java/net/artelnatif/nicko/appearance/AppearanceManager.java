package net.artelnatif.nicko.appearance;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoRemove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.artelnatif.nicko.storage.PlayerDataStore;
import net.artelnatif.nicko.storage.name.PlayerNameStore;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
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
        final UserProfile userProfile = new UserProfile(player.getUniqueId(), displayName);

        final ActionResult<Void> result = updateGameProfileSkin(userProfile, skinChange);
        if (!result.isError()) {
            updateTabList(userProfile, displayName);
        }
        return new ActionResult<>();
    }

    private ActionResult<Void> updateGameProfileSkin(UserProfile userProfile, boolean skinChange) {
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
                        final List<TextureProperty> properties = userProfile.getTextureProperties();
                        properties.clear();
                        properties.add(new TextureProperty("textures", skinResult.getValue(), skinResult.getSignature()));
                        Bukkit.broadcastMessage("Modified properties");
                    }
                }
                Bukkit.broadcastMessage("Respawning player");
                respawnPlayer();
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
        final World world = player.getWorld();
        final WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(
                SpigotConversionUtil.fromBukkitWorld(world),
                world.getName(),
                Difficulty.getById(world.getDifficulty().ordinal()),
                world.getSeed(),
                SpigotConversionUtil.fromBukkitGameMode(player.getGameMode()),
                null,
                false,
                false,
                false,
                null,
                null
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, respawn);
    }

    private void updateTabList(UserProfile userProfile, String displayName) {
        final WrapperPlayServerPlayerInfoUpdate infoAdd = new WrapperPlayServerPlayerInfoUpdate(EnumSet.of(
                WrapperPlayServerPlayerInfoUpdate.Action.ADD_PLAYER,
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_GAME_MODE,
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_DISPLAY_NAME,
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LISTED,
                WrapperPlayServerPlayerInfoUpdate.Action.UPDATE_LATENCY
        ), new WrapperPlayServerPlayerInfoUpdate.PlayerInfo(
                userProfile,
                true,
                0,
                SpigotConversionUtil.fromBukkitGameMode(player.getGameMode()),
                Component.text(displayName),
                null
        ));

        final WrapperPlayServerPlayerInfoRemove infoRemove = new WrapperPlayServerPlayerInfoRemove(player.getUniqueId());
        Bukkit.broadcastMessage("Updating tablist");
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, infoRemove);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, infoAdd);

    }
}
