package net.artelnatif.nicko.disguise;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.artelnatif.nicko.storage.PlayerDataStore;
import net.artelnatif.nicko.storage.name.PlayerNameStore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.IOException;
import java.util.Collection;
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
        final WrappedGameProfile gameProfile = new WrappedGameProfile(player.getUniqueId(), displayName);

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
                        final Collection<WrappedSignedProperty> properties = gameProfile.getProperties().values();
                        properties.clear();
                        properties.add(new WrappedSignedProperty("textures", skinResult.getValue(), skinResult.getSignature()));
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
        // TODO (Ineanto, 4/23/23): Respawn Packet
        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    private void updateTabList(WrappedGameProfile gameProfile, String displayName) {
        // TODO (Ineanto, 4/23/23): Update player info packet
        // TODO (Ineanto, 4/23/23): Remove player info packet
        Bukkit.broadcastMessage("Updating tablist");
        // TODO (Ineanto, 4/23/23): Send packets
    }
}
