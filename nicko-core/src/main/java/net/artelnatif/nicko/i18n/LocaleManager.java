package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;
import org.apache.commons.lang.LocaleUtils;

import java.util.Arrays;
import java.util.Locale;

public class LocaleManager {
    private static final String[] supportedLocales = new String[]{"en", "fr", "custom"};

    public static void setDefaultLocale(NickoBukkit instance) {
        final String locale = instance.getNickoConfig().getDefaultLocale();
        try {
            if (Arrays.stream(supportedLocales).noneMatch(s -> s.equalsIgnoreCase(locale))) {
                instance.getLogger().severe(locale + " is not a supported locale, defaulting to English.");
                Locale.setDefault(Locale.ENGLISH);
                return;
            }
            final Locale defaultLocale = LocaleUtils.toLocale(locale);
            instance.getLogger().info("Default locale set to " + defaultLocale.getDisplayName() + ".");
            Locale.setDefault(defaultLocale);
        } catch (Exception e) {
            instance.getLogger().severe(locale + " is not a valid locale, defaulting to English.");
            Locale.setDefault(Locale.ENGLISH);
        }
    }
}
