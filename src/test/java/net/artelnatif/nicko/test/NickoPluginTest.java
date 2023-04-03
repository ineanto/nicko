package net.artelnatif.nicko.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.config.DataSourceConfiguration;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NickoPluginTest {
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                DataSourceConfiguration.SQL_EMPTY,
                DataSourceConfiguration.REDIS_EMPTY,
                "",
                true,
                false);
        MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Plugin Initialization")
    public void initializePlugin() {
        assertNotNull(plugin);
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
