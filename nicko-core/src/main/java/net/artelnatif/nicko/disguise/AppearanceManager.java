package net.artelnatif.nicko.disguise;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.storage.PlayerDataStore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class "AppearanceManager {
    private final NickoProfile profile;
    private final Player player;
    private final NickoBukkit instance = NickoBukkit.getInstance();
    private final PlayerDataStore dataStore = instance.getDataStore();

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

    public UpdateResult reset() {
        final String defaultName = instance.getDataStore().getStoredName(player);
        this.profile.setName(defaultName);
        this.profile.setSkin(defaultName);
        final UpdateResult updateResult = updatePlayer(true);
        this.profile.setSkin(null);
        this.profile.setName(null);
        return updateResult;
    }

    public UpdateResult updatePlayer(boolean skinChange) {
        return NickoBukkit.getInstance().getInternals().updateProfile(player, profile, skinChange);
    }
}
