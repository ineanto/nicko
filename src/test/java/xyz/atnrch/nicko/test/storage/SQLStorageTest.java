package xyz.atnrch.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.*;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.DataSourceConfiguration;
import xyz.atnrch.nicko.appearance.ActionResult;
import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.atnrch.nicko.i18n.Locale;
import xyz.atnrch.nicko.storage.PlayerDataStore;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("Create tables")
    @Order(1)
    public void createTables() {
        assertFalse(plugin.getDataStore().getStorage().isError());
    }

    @Test
    @DisplayName("Store empty profile")
    @Order(2)
    public void storeEmptyProfile() {
        final Optional<NickoProfile> optionalProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(optionalProfile.isPresent());
    }

    @Test
    @DisplayName("Update profile")
    @Order(3)
    public void updateProfile() {
        final Optional<NickoProfile> optionalProfile = plugin.getDataStore().getData(player.getUniqueId());
        final NickoProfile profile = optionalProfile.get();
        assertNull(profile.getName());
        assertNull(profile.getSkin());
        assertEquals(profile.getLocale(), Locale.ENGLISH);
        assertTrue(profile.isBungeecordTransfer());

        profile.setName("Notch");
        profile.setSkin("Notch");
        profile.setLocale(Locale.FRENCH);
        profile.setBungeecordTransfer(false);

        final ActionResult result = plugin.getDataStore().saveData(player);
        assertFalse(result.isError());
    }

    @Test
    @DisplayName("Get updated profile")
    @Order(4)
    public void hasProfileBeenUpdated() {
        final Optional<NickoProfile> profile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(profile.isPresent());

        final NickoProfile updatedProfile = profile.get();
        assertEquals(updatedProfile.getName(), "Notch");
        assertEquals(updatedProfile.getSkin(), "Notch");
        assertEquals(updatedProfile.getLocale(), Locale.FRENCH);
        assertFalse(updatedProfile.isBungeecordTransfer());
    }

    @Test
    @DisplayName("Delete profile")
    @Order(5)
    public void deleteProfile() {
        final PlayerDataStore dataStore = plugin.getDataStore();
        final ActionResult sqlDelete = dataStore.getStorage().delete(player.getUniqueId());
        assertFalse(sqlDelete.isError());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}