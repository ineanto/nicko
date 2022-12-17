package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.apache.commons.lang3.LocaleUtils;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
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
            NickoBukkit.getInstance().getLogger().severe("Invalid locale provided by " + player.getName() + ", defaulting to " + Locale.getDefault().getCode() + ".");
            return Locale.getDefault();
        }
    }

    private static ResourceBundle getBundle(java.util.Locale locale) {
        return ResourceBundle.getBundle("locale", locale);
    }

    public static String translate(Player player, I18NDict key, Object... arguments) {
        final Locale locale = getLocale(player);
        String translation;
        if (locale == Locale.CUSTOM) {
            translation = "";
        } else {
            translation = getBundle(LocaleUtils.toLocale(locale.getCode())).getString(key.key());
        }

        try {
            formatter.applyPattern(translation);
            return NickoBukkit.getInstance().getNickoConfig().getPrefix() + formatter.format(arguments);
        } catch (Exception e) {
            return NickoBukkit.getInstance().getNickoConfig().getPrefix() + key.key();
        }
    }

    public static String translateFlat(Player player, I18NDict key, Object... arguments) {
        final Locale locale = getLocale(player);
        String translation;
        if (locale == Locale.CUSTOM) {
            translation = "";
        } else {
            translation = getBundle(LocaleUtils.toLocale(locale.getCode())).getString(key.key());
        }

        try {
            formatter.applyPattern(translation);
            return formatter.format(arguments);
        } catch (Exception e) {
            return key.key();
        }
    }
}
