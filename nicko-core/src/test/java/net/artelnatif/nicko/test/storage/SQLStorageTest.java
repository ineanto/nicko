package net.artelnatif.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.NickoConfiguration;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.junit.jupiter.api.*;

import java.util.Optional;

public class SQLStorageTest {
    private static ServerMock server;
    private static NickoBukkit plugin;
    private static NickoConfiguration config;

    @BeforeAll
    public static void setup() {
        server = MockBukkit.mock();
        config = new NickoConfiguration(null);
        config.setLocalStorage(false);
        config.setBungeecordSupport(false);
        config.setSQLAddress("127.0.0.1");
        config.setSQLUsername("root");
        config.setSQLPassword("12345"); // https://howsecureismypassword.net/ "Your password would be cracked: Instantly"
        plugin = MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Create SQL Tables")
    public void testSQLTables() {

        final PlayerMock playerMock = server.addPlayer("Aro");
        final Optional<NickoProfile> data = plugin.getDataStore().getData(playerMock.getUniqueId());
        Assertions.assertTrue(data.isPresent());
        Assertions.assertNull(data.get().getSkin());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
