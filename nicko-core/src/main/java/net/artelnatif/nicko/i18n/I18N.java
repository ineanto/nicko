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
        final NickoBukkit instance = NickoBukkit.getInstance();
        try {
            final Optional<NickoProfile> profile = instance.getDataStore().getData(player.getUniqueId());
            if (profile.isEmpty()) {
                return Locale.ENGLISH;
            } else {
                return profile.get().getLocale();
            }
        } catch (IllegalArgumentException exception) {
            instance.getLogger().severe("Invalid locale provided by " + player.getName() + ", defaulting to " + LocaleManager.getFallback().getCode() + ".");
            return LocaleManager.getFallback();
        }
    }

    private static ResourceBundle getBundle(java.util.Locale locale) {
        return ResourceBundle.getBundle("locale", locale);
    }

    public static String translate(Player player, I18NDict key, Object... arguments) {
        final NickoBukkit instance = NickoBukkit.getInstance();
        final String translation = findTranslation(player, key);

        try {
            formatter.applyPattern(translation);
            return instance.getNickoConfig().getPrefix() + formatter.format(arguments);
        } catch (Exception e) {
            return instance.getNickoConfig().getPrefix() + key.key();
        }
    }

    public static String translateFlat(Player player, I18NDict key, Object... arguments) {
        final String translation = findTranslation(player, key);
        try {
            formatter.applyPattern(translation);
            return formatter.format(arguments);
        } catch (Exception e) {
            return key.key();
        }
    }


    private static String findTranslation(Player player, I18NDict key) {
        final NickoBukkit instance = NickoBukkit.getInstance();
        final Locale locale = getLocale(player);
        String translation;
        if (locale == Locale.CUSTOM) {
            translation = instance.getLocaleManager().getCustomLanguageFile().getProperty(key.key(), key.key());
        } else {
            translation = getBundle(LocaleUtils.toLocale(locale.getCode())).getString(key.key());
        }

        return translation;
    }
}
