package net.artelnatif.nicko.i18n;

import de.studiocode.invui.util.IOUtils;
import net.artelnatif.nicko.NickoBukkit;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class LocaleManager {
    private static Locale fallback;

    private final NickoBukkit instance;
    private final String[] supportedLocales = new String[]{"en", "fr", "custom"};
    private final File directory = new File(NickoBukkit.getInstance().getDataFolder() + "/lang/");
    private final File file = new File(directory, "custom.properties");

    private Properties customLanguageFile;

    public LocaleManager(NickoBukkit instance) {
        this.instance = instance;
    }

    public static void setFallback(Locale fallback) {
        LocaleManager.fallback = fallback;
    }

    public static Locale getFallback() {
        return fallback;
    }

    public void installCustomLanguageFile() {
        try (InputStream stream = this.getClass().getResourceAsStream("locale_en.properties")) {
            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                if (stream != null) {
                    IOUtils.copy(stream, outputStream, 8 * 1024);
                    instance.getLogger().info("Installing custom language file as \"custom.properties\"");
                }
            }
        } catch (IOException e) {
            instance.getLogger().warning("Couldn't get English resource file! Skipping.");
            if (LocaleManager.getFallback() != Locale.ENGLISH) {
                LocaleManager.setFallback(Locale.ENGLISH);
            }
            throw new RuntimeException(e);
        }
    }

    public Properties getCustomLanguageFile() {
        if (customLanguageFile == null) {
            final Properties properties = new Properties();
            try {
                properties.load(new BufferedInputStream(new FileInputStream(file)));
                return customLanguageFile = properties;
            } catch (IOException e) {
                instance.getLogger().warning("Couldn't load Custom properties language file, falling back to English.");
                if (LocaleManager.getFallback() != Locale.ENGLISH) {
                    LocaleManager.setFallback(Locale.ENGLISH);
                }
                return customLanguageFile = null;
            }
        }

        return customLanguageFile;
    }

    public Properties reloadCustomLanguageFile() {
        customLanguageFile = null;
        return getCustomLanguageFile();
    }

    public void findFallbackLocale() {
        final String locale = instance.getNickoConfig().getFallbackLocale();
        try {
            if (Arrays.stream(supportedLocales).noneMatch(s -> s.equalsIgnoreCase(locale))) {
                instance.getLogger().severe(locale + " is not a supported locale, defaulting to English.");
                LocaleManager.setFallback(Locale.ENGLISH);
                return;
            }
            final Locale defaultLocale = Locale.fromCode(locale);
            instance.getLogger().info("Fallback locale set to " + defaultLocale.getName() + ".");
            LocaleManager.setFallback(defaultLocale);
        } catch (Exception e) {
            instance.getLogger().severe(locale + " is not a valid locale, defaulting to English.");
            LocaleManager.setFallback(Locale.ENGLISH);
        }
    }
}
