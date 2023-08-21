package xyz.atnrch.nicko.profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.i18n.Locale;
import xyz.atnrch.nicko.storage.PlayerDataStore;

import java.util.Optional;
import java.util.UUID;

public class NickoProfile implements Cloneable {
    public static final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
    public static final NickoProfile EMPTY_PROFILE = new NickoProfile(null, null, Locale.ENGLISH, true);

    private transient final Player player;

    private String name;
    private String skin;
    private Locale locale;
    private boolean bungeecordTransfer;

    public NickoProfile(String name, String skin, Locale locale, boolean bungeecordTransfer) {
        this.name = name;
        this.skin = skin;
        this.locale = locale;
        this.bungeecordTransfer = bungeecordTransfer;
        this.player = null;
    }

    public NickoProfile(Player player, String name, String skin, Locale locale, boolean bungeecordTransfer) {
        this.player = player;
        this.name = name;
        this.skin = skin;
        this.locale = locale;
        this.bungeecordTransfer = bungeecordTransfer;
    }

    public NickoProfile(UUID uuid, String name, String skin, Locale locale, boolean bungeecordTransfer) {
        this.name = name;
        this.skin = skin;
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
               "name='" + name + '\'' +
               ", skin='" + skin + '\'' +
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
