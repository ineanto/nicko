package xyz.atnrch.nicko.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.AppearanceManager;
import xyz.atnrch.nicko.i18n.Locale;
import xyz.atnrch.nicko.storage.PlayerDataStore;

import java.util.Optional;
import java.util.UUID;

public class NickoProfile implements Cloneable {
    public static final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
    public static final NickoProfile EMPTY_PROFILE = new NickoProfile(new AppearanceData(null, null), Locale.ENGLISH, true);

    private final AppearanceData appearanceData;
    private final Player player;
    private Locale locale;
    private boolean bungeecordTransfer;

    public NickoProfile(AppearanceData appearanceData, Locale locale, boolean bungeecordTransfer) {
        this.appearanceData = appearanceData;
        this.locale = locale;
        this.bungeecordTransfer = bungeecordTransfer;
        this.player = null;
    }

    public NickoProfile(Player player, AppearanceData appearanceData, Locale locale, boolean bungeecordTransfer) {
        this.player = player;
        this.appearanceData = appearanceData;
        this.locale = locale;
        this.bungeecordTransfer = bungeecordTransfer;
    }

    public NickoProfile(UUID uuid, AppearanceData appearanceData, Locale locale, boolean bungeecordTransfer) {
        this.appearanceData = appearanceData;
        this.locale = locale;
        this.bungeecordTransfer = bungeecordTransfer;
        this.player = Bukkit.getPlayer(uuid);
    }

    public static Optional<NickoProfile> get(Player player) {
        return dataStore.getData(player.getUniqueId());
    }

    public static Optional<NickoProfile> get(UUID uuid) {
        return dataStore.getData(uuid);
    }

    public AppearanceManager getAppearanceManager() {
        if (player == null) return null;
        return new AppearanceManager(player);
    }

    public AppearanceData getAppearanceData() {
        return appearanceData;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    public boolean isBungeecordTransfer() {
        return bungeecordTransfer;
    }

    public void setBungeecordTransfer(boolean bungeecordTransfer) {
        this.bungeecordTransfer = bungeecordTransfer;
    }

    @Override
    public String toString() {
        return "NickoProfile{" +
               "name='" + appearanceData.getName() + '\'' +
               ", skin='" + appearanceData.getSkin() + '\'' +
               ", locale=" + locale +
               ", bungeecordTransfer=" + bungeecordTransfer +
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
