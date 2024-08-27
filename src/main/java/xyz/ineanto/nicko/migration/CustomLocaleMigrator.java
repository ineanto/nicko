package xyz.ineanto.nicko.migration;

import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.language.CustomLanguage;
import xyz.ineanto.nicko.language.Language;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CustomLocaleMigrator implements Migrator {
    private final Nicko instance;
    private final CustomLanguage customLanguage;

    public CustomLocaleMigrator(Nicko instance, CustomLanguage customLanguage) {
        this.instance = instance;
        this.customLanguage = customLanguage;
    }

    @Override
    public void migrate() {
        // Migrate custom locale (1.1.0-RC1)
        if (customLanguage.getVersionObject() == null
            || customLanguage.getVersion().isEmpty()
            || customLanguage.getVersionObject().compareTo(Language.VERSION) != 0) {
            instance.getLogger().info("Migrating the custom locale (" + customLanguage.getVersion() + ") to match the current version (" + Language.VERSION + ")...");

            final String date = Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            final File backupFile = new File(customLanguage.getDirectory(), "locale-" + date + ".yml");

            try {
                Files.copy(customLanguage.getFile().toPath(), backupFile.toPath());
                if (customLanguage.getFile().delete()) {
                    CustomLanguage.dumpIntoFile(Language.ENGLISH);
                    instance.getLogger().info("Successfully migrated the custom locale.");
                } else {
                    instance.getLogger().severe("Failed to migrate the custom locale!");
                }
            } catch (IOException e) {
                instance.getLogger().severe("Failed to migrate the custom locale!");
            }
        }
    }
}
