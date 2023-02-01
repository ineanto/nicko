package net.artelnatif.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.junit.jupiter.api.*;

import java.util.Optional;

public class BrokenSQLTest {
    private static ServerMock server;
    private static NickoBukkit plugin;
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                "127.0.0.1",
                "root",
                "INVALID_PASSWORD",
                "",
                false,
                false,
                false);
        server = MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
        player = server.addPlayer();
    }

    @Test
    @DisplayName("Fail to create Tables")
    public void createSQLTables() {
        Assertions.assertTrue(plugin.getNicko().getDataStore().getStorage().isError());
    }

    @Test
    @DisplayName("Fail to Store Player Via SQL")
    public void storePlayer() {
        final Optional<NickoProfile> optionalProfile = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertFalse(optionalProfile.isPresent());
        ActionResult<Void> result = plugin.getNicko().getDataStore().saveData(player);
        Assertions.assertTrue(result.isError());
    }

    @Test
    @DisplayName("Fail to Retrieve Player Via SQL")
    public void retrievePlayer() {
        final Optional<NickoProfile> storeAction = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertFalse(storeAction.isPresent());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}