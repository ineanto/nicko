package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;

import java.util.Arrays;

public class LocaleManager {
    private final NickoBukkit instance;
    private final String[] supportedLocales = new String[]{"en", "fr", "custom"};

    public LocaleManager(NickoBukkit instance) {
        this.instance = instance;

    }

    public void findFallbackLocale() {
        final String locale = instance.getNickoConfig().getFallbackLocale();
        try {
            if (Arrays.stream(supportedLocales).noneMatch(s -> s.equalsIgnoreCase(locale))) {
                instance.getLogger().severe(locale + " is not a supported locale, defaulting to English.");
                Locale.setFallback(Locale.ENGLISH);
                return;
            }
            final Locale defaultLocale = Locale.fromCode(locale);
            instance.getLogger().info("Fallback locale set to " + defaultLocale.getName() + ".");
            Locale.setFallback(defaultLocale);
        } catch (Exception e) {
            instance.getLogger().severe(locale + " is not a valid locale, defaulting to English.");
            Locale.setFallback(Locale.ENGLISH);
        }
    }
}
