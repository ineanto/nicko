package xyz.ineanto.nicko.migration;

import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.ConfigurationManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigurationMigrator implements Migrator {
    private final NickoBukkit instance;

    public ConfigurationMigrator(NickoBukkit instance) {
        this.instance = instance;
    }

    @Override
    public void migrate() {
        final Configuration configuration = instance.getNickoConfig();
        final ConfigurationManager configurationManager = instance.getConfigurationManager();

        // Migrate configuration (1.0.8-RC1)
        if (configuration.getVersion() == null
            || configuration.getVersion().isEmpty()
            || configuration.getVersionObject().compareTo(Configuration.VERSION) != 0) {
            instance.getLogger().info("Migrating configuration file to match the current version...");
            try {
                Files.copy(configurationManager.getFile().toPath(), configurationManager.getBackupFile().toPath(), StandardCopyOption.ATOMIC_MOVE);
                configurationManager.saveDefaultConfig();
            } catch (IOException e) {
                instance.getLogger().severe("Failed to migrate your configuration!");
            }
        }
    }
}
