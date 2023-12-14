package xyz.ineanto.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.*;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DefaultDataSources;
import xyz.ineanto.nicko.config.SQLDataSourceConfiguration;
import xyz.ineanto.nicko.profile.NickoProfile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
public class BrokenSQLTest {
    private static NickoBukkit plugin;
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                new SQLDataSourceConfiguration(true, "127.0.0.1", 3306, "root", "", true),
                DefaultDataSources.REDIS_EMPTY,
                "",
                false);
        final ServerMock server = MockBukkit.mock();
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
        ActionResult result = plugin.getDataStore().saveData(player);
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