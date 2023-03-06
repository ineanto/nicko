package net.artelnatif.nicko.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.Configuration;
import org.junit.jupiter.api.*;

public class NickoPluginTest {
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                "",
                "",
                "",
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
        Assertions.assertNotNull(plugin.getConfig());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
