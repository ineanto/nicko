package xyz.ineanto.nicko.profile;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.Appearance;
import xyz.ineanto.nicko.language.Language;
import xyz.ineanto.nicko.storage.PlayerDataStore;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NickoProfile implements Cloneable {
    public static final NickoProfile EMPTY_PROFILE = new NickoProfile(
            new Appearance(null, null),
            Language.ENGLISH,
            true,
            Collections.emptyList()
    );

    private static final Nicko instance = Nicko.getInstance();
    private static final PlayerDataStore dataStore = instance.getDataStore();

    private Appearance appearance;
    private Language language;
    private boolean randomSkin;
    private List<Appearance> favorites;

    public NickoProfile(Appearance appearance, Language language, boolean randomSkin, List<Appearance> favorites) {
        this.appearance = appearance;
        this.language = language;
        this.randomSkin = randomSkin;
        this.favorites = favorites;
    }

    public static Optional<NickoProfile> get(Player player) {
        return dataStore.getData(player.getUniqueId());
    }

    public static Optional<NickoProfile> get(UUID uuid) {
        return dataStore.getData(uuid);
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public boolean hasData() {
        return appearance.name() != null || appearance.skin() != null;
    }

    public String getName() {
        return appearance.name();
    }

    public void setName(String name) {
        this.appearance = new Appearance(name, appearance.skin() == null ? null : appearance.skin());
    }

    public String getSkin() {
        return appearance.skin();
    }

    public void setSkin(String skin) {
        this.appearance = new Appearance(appearance.name() == null ? null : appearance.name(), skin);
    }

    public List<Appearance> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Appearance> favorites) {
        this.favorites = favorites;
    }

    public Language getLocale() {
        return language;
    }

    public void setLocale(Language language) {
        this.language = language;
    }

    public boolean isRandomSkin() {
        return randomSkin;
    }

    public void setRandomSkin(boolean randomSkin) {
        this.randomSkin = randomSkin;
    }

    @Override
    public NickoProfile clone() {
        Object o;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return (NickoProfile) o;
    }
}
