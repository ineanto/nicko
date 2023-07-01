package xyz.atnrch.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.*;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.DataSourceConfiguration;
import xyz.atnrch.nicko.disguise.ActionResult;
import xyz.atnrch.nicko.disguise.NickoProfile;
import xyz.atnrch.nicko.i18n.Locale;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLStorageTest {
    private static ServerMock server;
    private static NickoBukkit plugin;
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                new DataSourceConfiguration(true, "127.0.0.1", 3306, "root", "12345"),
                DataSourceConfiguration.REDIS_EMPTY,
                "",
                false);
        server = MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
        player = server.addPlayer();
    }

    @Test
    @DisplayName("Create SQL Tables")
    @Order(1)
    public void createSQLTables() {
        assertFalse(plugin.getDataStore().getStorage().isError());
    }

    @Test
    @DisplayName("Store Player Via SQL")
    @Order(2)
    public void storePlayer() {
        final Optional<NickoProfile> optionalProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(optionalProfile.isPresent());
    }

    @Test
    @DisplayName("Retrieve Player Via SQL")
    @Order(3)
    public void retrievePlayer() {
        final Optional<NickoProfile> storeAction = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(storeAction.isPresent());
    }

    @Test
    @DisplayName("Update Player Via SQL")
    @Order(4)
    public void updatePlayer() {
        final Optional<NickoProfile> optionalProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(optionalProfile.isPresent());

        final NickoProfile profile = optionalProfile.get();
        Assertions.assertNull(profile.getName());
        Assertions.assertNull(profile.getSkin());
        Assertions.assertEquals(profile.getLocale(), Locale.ENGLISH);
        assertTrue(profile.isBungeecordTransfer());

        profile.setName("Notch");
        profile.setSkin("Notch");
        profile.setLocale(Locale.FRENCH);
        profile.setBungeecordTransfer(false);

        final ActionResult result = plugin.getDataStore().saveData(player);
        assertFalse(result.isError());

        final Optional<NickoProfile> optionalUpdatedProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(optionalUpdatedProfile.isPresent());
        final NickoProfile updatedProfile = optionalProfile.get();
        Assertions.assertEquals(updatedProfile.getName(), "Notch");
        Assertions.assertEquals(updatedProfile.getSkin(), "Notch");
        Assertions.assertEquals(updatedProfile.getLocale(), Locale.FRENCH);
        assertFalse(updatedProfile.isBungeecordTransfer());
    }

    @Test
    @DisplayName("Remove Player Disguise Via SQL")
    @Order(5)
    public void removePlayerDisguise() {
        final Optional<NickoProfile> optionalProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(optionalProfile.isPresent());

        final NickoProfile profile = optionalProfile.get();

        profile.setName(null);
        profile.setSkin(null);

        final ActionResult result = plugin.getDataStore().saveData(player);
        assertFalse(result.isError());

        final Optional<NickoProfile> optionalUpdatedProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(optionalUpdatedProfile.isPresent());
        final NickoProfile updatedProfile = optionalProfile.get();
        Assertions.assertNull(updatedProfile.getName());
        Assertions.assertNull(updatedProfile.getSkin());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}