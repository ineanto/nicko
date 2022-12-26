package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        final InputStream resource = instance.getResource("locale_en.properties");

        try {
            instance.getLogger().info("Installing custom language file as \"custom.properties\"");
            // TODO: 12/26/22 This throws an error!
            Files.copy(resource, Paths.get(file.toURI()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // TODO: 12/19/22 Handle error
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
