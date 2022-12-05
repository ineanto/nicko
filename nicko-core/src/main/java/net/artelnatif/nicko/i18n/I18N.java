package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class I18N {
    private final static MessageFormat formatter = new MessageFormat("");

    private static Locale getLocale(Player player) {
        try {
            final Optional<NickoProfile> profile = NickoBukkit.getInstance().getDataStore().getData(player.getUniqueId());
            if (profile.isEmpty()) {
                return Locale.ENGLISH;
            } else {
                return profile.get().getLocale();
            }
        } catch (IllegalArgumentException exception) {
            NickoBukkit.getInstance().getLogger().severe("Invalid locale provided, defaulting to " + Locale.getDefault().getDisplayName() + ".");
            return Locale.getDefault();
        }
    }

    private static ResourceBundle getBundle(Player player) {
        return ResourceBundle.getBundle("locale", getLocale(player));
    }

    public static String translate(Player player, I18NDict key, Object... arguments) {
        try {
            formatter.applyPattern(getBundle(player).getString(key.key()));
            return NickoBukkit.getInstance().getNickoConfig().getPrefix() + formatter.format(arguments);
        } catch (Exception e) {
            return NickoBukkit.getInstance().getNickoConfig().getPrefix() + key.key();
        }
    }
}
