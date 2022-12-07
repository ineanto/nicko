package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;

import java.util.Arrays;

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
            final Locale defaultLocale = Locale.fromCode(locale);
            instance.getLogger().info("Default locale set to " + defaultLocale.getName() + ".");
            Locale.setDefault(defaultLocale);
        } catch (Exception e) {
            instance.getLogger().severe(locale + " is not a valid locale, defaulting to English.");
            Locale.setDefault(Locale.ENGLISH);
        }
    }
}
