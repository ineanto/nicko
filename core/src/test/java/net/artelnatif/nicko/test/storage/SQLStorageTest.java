package net.artelnatif.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.bukkit.i18n.Locale;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.junit.jupiter.api.*;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    public void createSQLTables() {
        Assertions.assertFalse(plugin.getNicko().getDataStore().getStorage().isError());
    }

    @Test
    @DisplayName("Store Player Via SQL")
    @Order(2)
    public void storePlayer() {
        final Optional<NickoProfile> optionalProfile = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertTrue(optionalProfile.isPresent());
    }

    @Test
    @DisplayName("Retrieve Player Via SQL")
    @Order(3)
    public void retrievePlayer() {
        final Optional<NickoProfile> storeAction = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertTrue(storeAction.isPresent());
    }

    @Test
    @DisplayName("Update Player Via SQL")
    @Order(4)
    public void updatePlayer() {
        final Optional<NickoProfile> optionalProfile = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertTrue(optionalProfile.isPresent());

        final NickoProfile profile = optionalProfile.get();
        Assertions.assertNull(profile.getName());
        Assertions.assertNull(profile.getSkin());
        Assertions.assertEquals(profile.getLocale(), Locale.ENGLISH);
        Assertions.assertTrue(profile.isBungeecordTransfer());

        profile.setName("Notch");
        profile.setSkin("Notch");
        profile.setLocale(Locale.FRENCH);
        profile.setBungeecordTransfer(false);

        final ActionResult<Void> result = plugin.getNicko().getDataStore().saveData(player);
        Assertions.assertFalse(result.isError());

        final Optional<NickoProfile> optionalUpdatedProfile = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertTrue(optionalUpdatedProfile.isPresent());
        final NickoProfile updatedProfile = optionalProfile.get();
        Assertions.assertEquals(updatedProfile.getName(), "Notch");
        Assertions.assertEquals(updatedProfile.getSkin(), "Notch");
        Assertions.assertEquals(updatedProfile.getLocale(), Locale.FRENCH);
        Assertions.assertFalse(updatedProfile.isBungeecordTransfer());
    }

    @Test
    @DisplayName("Remove Player Disguise Via SQL")
    @Order(5)
    public void removePlayerDisguise() {
        final Optional<NickoProfile> optionalProfile = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertTrue(optionalProfile.isPresent());

        final NickoProfile profile = optionalProfile.get();

        profile.setName(null);
        profile.setSkin(null);

        final ActionResult<Void> result = plugin.getNicko().getDataStore().saveData(player);
        Assertions.assertFalse(result.isError());

        final Optional<NickoProfile> optionalUpdatedProfile = plugin.getNicko().getDataStore().getData(player.getUniqueId());
        Assertions.assertTrue(optionalUpdatedProfile.isPresent());
        final NickoProfile updatedProfile = optionalProfile.get();
        Assertions.assertNull(updatedProfile.getName());
        Assertions.assertNull(updatedProfile.getSkin());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}