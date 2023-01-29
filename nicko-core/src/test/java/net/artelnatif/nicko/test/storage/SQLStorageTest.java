package net.artelnatif.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.Locale;
import org.junit.jupiter.api.*;

public class SQLStorageTest {
    private static ServerMock server;
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                "127.0.0.1",
                "root",
                "12345",
                "",
                false,
                false,
                false);
        server = MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Create SQL Tables")
    public void createSQLTables() {
        Assertions.assertFalse(plugin.getNicko().getDataStore().getStorage().isError());
    }

    @Test
    @DisplayName("Store Player Via SQL")
    public void storePlayer() {
        final PlayerMock playerMock = server.addPlayer();
        final NickoProfile profile = new NickoProfile("Notch", "Notch", Locale.ENGLISH, true);
        final ActionResult<Void> storeAction = plugin.getNicko().getDataStore().getStorage().store(playerMock.getUniqueId(), profile);
        Assertions.assertFalse(storeAction.isError());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
