package net.artelnatif.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.NickoConfiguration;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.Locale;
import org.junit.jupiter.api.*;

public class BrokenSQLTest {
    private static ServerMock server;
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        final NickoConfiguration config = new NickoConfiguration(null);
        config.setLocalStorage(false);
        config.setBungeecordSupport(false);
        config.setSQLAddress("127.0.0.1");
        config.setSQLUsername("root");
        config.setSQLPassword("INVALID_PASSWORD");

        server = MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Fail to create Tables")
    public void createSQLTables() {
        Assertions.assertTrue(plugin.getDataStore().getStorage().isError());
    }

    @Test
    @DisplayName("Store Player Via SQL")
    public void storePlayer() {
        final PlayerMock playerMock = server.addPlayer();
        final NickoProfile profile = new NickoProfile("Notch", "Notch", Locale.ENGLISH, true);
        final ActionResult<Void> storeAction = plugin.getDataStore().getStorage().store(playerMock.getUniqueId(), profile);
        Assertions.assertFalse(storeAction.isError());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}