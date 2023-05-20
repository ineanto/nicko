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
                new DataSourceConfiguration(true, "127.0.0.1", 3306, "root", "12345"),
                DataSourceConfiguration.REDIS_EMPTY,
                "",
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
