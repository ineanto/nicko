package net.artelnatif.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.config.DataSourceConfiguration;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BrokenSQLTest {
    private static ServerMock server;
    private static NickoBukkit plugin;
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                DataSourceConfiguration.SQL_EMPTY,
                DataSourceConfiguration.REDIS_EMPTY,
                "",
                false,
                false);
        server = MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
        player = server.addPlayer();
    }

    @Test
    @DisplayName("Fail to create Tables")
    public void createSQLTables() {
        assertTrue(plugin.getDataStore().getStorage().isError());
    }

    @Test
    @DisplayName("Fail to Store Player Via SQL")
    public void storePlayer() {
        final Optional<NickoProfile> optionalProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertFalse(optionalProfile.isPresent());
        ActionResult<Void> result = plugin.getDataStore().saveData(player);
        assertTrue(result.isError());
    }

    @Test
    @DisplayName("Fail to Retrieve Player Via SQL")
    public void retrievePlayer() {
        final Optional<NickoProfile> storeAction = plugin.getDataStore().getData(player.getUniqueId());
        assertFalse(storeAction.isPresent());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}