package xyz.ineanto.nicko.migration;

import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.i18n.CustomLocale;
import xyz.ineanto.nicko.i18n.Locale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CustomLocaleMigrator implements Migrator {
    private final NickoBukkit instance;

    public CustomLocaleMigrator(NickoBukkit instance) {
        this.instance = instance;
    }

    @Override
    public void migrate() {
        final CustomLocale customLanguageFile = instance.getCustomLocale();

        // Migrate custom locale (1.1.0-RC1)
        if (customLanguageFile.getVersionObject() == null
            || customLanguageFile.getVersion().isEmpty()
            || customLanguageFile.getVersionObject().compareTo(Locale.VERSION) != 0) {
            instance.getLogger().info("Migrating custom locale to match the current version...");
            try {
                Files.copy(customLanguageFile.getFile().toPath(), customLanguageFile.getBackupFile().toPath(), StandardCopyOption.ATOMIC_MOVE);
                customLanguageFile.dumpIntoFile(Locale.ENGLISH);
            } catch (IOException e) {
                instance.getLogger().severe("Failed to migrate your custom locale!");
            }
        }
    }
}
