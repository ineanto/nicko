package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18N {
    private final static MessageFormat formatter = new MessageFormat("");

    private static Locale getLocale(Player player) {
        try {
            return LocaleUtils.toLocale(player.getLocale().substring(0, 1));
        } catch (IllegalArgumentException exception) {
            NickoBukkit.getInstance().getLogger().severe("Invalid locale provided, defaulting to " + Locale.getDefault().getDisplayName() + ".");
            return Locale.getDefault();
        }
    }

    private static ResourceBundle getBundle(Player player) {
        return ResourceBundle.getBundle("locale", getLocale(player));
    }

    public static String translate(Player player, String key, Object... arguments) {
        try {
            formatter.applyPattern(getBundle(player).getString(key));
            return formatter.format(arguments);
        } catch (Exception e) {
            return key;
        }
    }
}
