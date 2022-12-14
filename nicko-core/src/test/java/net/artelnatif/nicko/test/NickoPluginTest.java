package net.artelnatif.nicko.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.NickoConfiguration;
import org.junit.jupiter.api.*;

public class NickoPluginTest {
    private static ServerMock server;
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        final NickoConfiguration config = new NickoConfiguration(null);
        config.setLocalStorage(true);
        config.setBungeecordSupport(false);
        server = MockBukkit.mock();
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
