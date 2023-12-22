package xyz.ineanto.nicko.test.migration;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DefaultDataSources;
import xyz.ineanto.nicko.i18n.CustomLocale;
import xyz.ineanto.nicko.migration.CustomLocaleMigrator;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MigrationTest {
    private static NickoBukkit plugin;

    private static File folder;
    private static File localeFile;

    @BeforeAll
    public static void setup() throws IOException {
        MockBukkit.mock();
        final Configuration configuration = new Configuration(Configuration.VERSION.toString(),
                DefaultDataSources.SQL_EMPTY,
                DefaultDataSources.REDIS_EMPTY,
                "§6Nicko §8§l| §r",
                true);
        plugin = MockBukkit.load(NickoBukkit.class, configuration);
        folder = new File(plugin.getDataFolder(), "/locale/");
        localeFile = new File(folder, "locale.yml");
        folder.mkdirs();
        localeFile.createNewFile();
    }

    @Test
    public void testLanguageFileMigration() throws IOException {
        final String content = """
                # Nicko - Language File:
                                 
                # hello I'm the invalid version
                version: "1.0.0"
                 """;

        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localeFile));
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();

        // Get wrong locale
        final CustomLocale customLocaleBeforeMigration = new CustomLocale();
        assertEquals(customLocaleBeforeMigration.getVersion(), "1.0.0");

        // Migrate the wrong locale to the correct one
        final CustomLocaleMigrator localeMigrator = new CustomLocaleMigrator(plugin, customLocaleBeforeMigration);
        localeMigrator.migrate();

        // Get the migrated locale
        final CustomLocale customLocaleMigrated = new CustomLocale();
        assertEquals(customLocaleMigrated.getVersion(), "1.1.0");
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
        folder.delete();
        localeFile.delete();
    }
}
