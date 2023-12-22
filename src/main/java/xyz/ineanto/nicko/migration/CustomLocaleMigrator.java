package xyz.ineanto.nicko.migration;

import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.i18n.CustomLocale;
import xyz.ineanto.nicko.i18n.Locale;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CustomLocaleMigrator implements Migrator {
    private final NickoBukkit instance;
    private final CustomLocale customLocale;

    public CustomLocaleMigrator(NickoBukkit instance, CustomLocale customLocale) {
        this.instance = instance;
        this.customLocale = customLocale;
    }

    @Override
    public void migrate() {
        // Migrate custom locale (1.1.0-RC1)
        if (customLocale.getVersionObject() == null
            || customLocale.getVersion().isEmpty()
            || customLocale.getVersionObject().compareTo(Locale.VERSION) != 0) {
            instance.getLogger().info("Migrating the custom locale (" + customLocale.getVersion() + ") to match the current version (" + Locale.VERSION + ")...");

            final String date = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            final File backupFile = new File(customLocale.getDirectory(), "locale-" + date + ".yml");

            try {
                Files.copy(customLocale.getFile().toPath(), backupFile.toPath());
                if (customLocale.getFile().delete()) {
                    customLocale.dumpIntoFile(Locale.ENGLISH);
                }
                instance.getLogger().info("Successfully migrated the custom locale.");
            } catch (IOException e) {
                instance.getLogger().severe("Failed to migrate the custom locale!");
            }
        }
    }
}
