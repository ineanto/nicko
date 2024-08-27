package xyz.ineanto.nicko.profile;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.language.Language;
import xyz.ineanto.nicko.storage.PlayerDataStore;

import java.util.Optional;
import java.util.UUID;

public class NickoProfile implements Cloneable {
    public static final PlayerDataStore dataStore = Nicko.getInstance().getDataStore();
    public static final NickoProfile EMPTY_PROFILE = new NickoProfile(null, null, Language.ENGLISH, true);

    private String name;
    private String skin;
    private Language language;
    private boolean randomSkin;

    public NickoProfile(String name, String skin, Language language, boolean randomSkin) {
        this.name = name;
        this.skin = skin;
        this.language = language;
        this.randomSkin = randomSkin;
    }

    public static Optional<NickoProfile> get(Player player) {
        return dataStore.getData(player.getUniqueId());
    }

    public static Optional<NickoProfile> get(UUID uuid) {
        return dataStore.getData(uuid);
    }

    public boolean hasData() {
        return name != null || skin != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
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
    public String toString() {
        return "NickoProfile{" +
               "name='" + name + '\'' +
               ", skin='" + skin + '\'' +
               ", locale=" + language +
               ", randomSkin=" + randomSkin +
               '}';
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
