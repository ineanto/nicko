package xyz.atnrch.nicko.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.DataSourceConfiguration;
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
