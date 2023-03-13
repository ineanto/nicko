package net.artelnatif.nicko.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.config.DataSourceConfiguration;
import org.junit.jupiter.api.*;

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
    public void testPluginInitialization() {
        Assertions.assertNotNull(plugin.getDataStore().getStorage().getProvider());
        Assertions.assertNotNull(plugin.getNickoConfig());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
