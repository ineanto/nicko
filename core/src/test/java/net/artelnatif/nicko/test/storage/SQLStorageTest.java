package net.artelnatif.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.bukkit.i18n.Locale;
import org.junit.jupiter.api.*;

import java.util.Optional;

public class SQLStorageTest {
    private static ServerMock server;
    private static NickoBukkit plugin;
    private static PlayerMock player;

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
        player = server.addPlayer();
    }

    @Test
    @DisplayName("Create SQL Tables")
    public void createSQLTables() {
        Assertions.assertFalse(plugin.getNicko().getDataStore().getStorage().isError());
    }

    @Test
    @DisplayName("Store Player Via SQL")
    public void storePlayer() {
        final Optional<NickoProfile> optionalProfile = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertTrue(optionalProfile.isPresent());

        final NickoProfile profile = optionalProfile.get();
        profile.setName("Notch");
        profile.setSkin("Notch");
        profile.setLocale(Locale.ENGLISH);
        profile.setBungeecordTransfer(true);
        ActionResult<Void> result = plugin.getNicko().getDataStore().saveData(player);
        Assertions.assertFalse(result.isError());
    }

    @Test
    @DisplayName("Retrieve Player Via SQL")
    public void retrievePlayer() {
        final Optional<NickoProfile> storeAction = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertTrue(storeAction.isPresent());
        final NickoProfile profile = storeAction.get();
        Assertions.assertEquals("Notch", profile.getName());
        Assertions.assertEquals(Locale.ENGLISH, profile.getLocale());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}