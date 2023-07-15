package xyz.atnrch.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.*;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.ActionResult;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.DataSourceConfiguration;
import xyz.atnrch.nicko.i18n.Locale;
import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.atnrch.nicko.storage.PlayerDataStore;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SQLStorageTest {
    private static PlayerDataStore dataStore;
    private static UUID uuid;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                new DataSourceConfiguration(true, "127.0.0.1", 3306, "root", "12345"),
                DataSourceConfiguration.REDIS_EMPTY,
                "",
                false);

        MockBukkit.mock();

        final NickoBukkit plugin = MockBukkit.load(NickoBukkit.class, config);
        dataStore = plugin.getDataStore();
        uuid = UUID.randomUUID();
    }

    @Test
    @DisplayName("Create tables")
    @Order(1)
    public void createTables() {
        assertFalse(dataStore.getStorage().isError());
    }

    @Test
    @DisplayName("Store empty profile")
    @Order(2)
    public void storeEmptyProfile() {
        final Optional<NickoProfile> optionalProfile = dataStore.getData(uuid);
        assertTrue(optionalProfile.isPresent());
    }

    @Test
    @DisplayName("Update profile")
    @Order(3)
    public void updateProfile() {
        final Optional<NickoProfile> optionalProfile = dataStore.getData(uuid);
        final NickoProfile profile = optionalProfile.get();
        assertNull(profile.getName());
        assertNull(profile.getSkin());
        assertEquals(profile.getLocale(), Locale.ENGLISH);
        assertTrue(profile.isBungeecordTransfer());

        profile.setName("Notch");
        profile.setSkin("Notch");
        profile.setLocale(Locale.FRENCH);
        profile.setBungeecordTransfer(false);

        final ActionResult result = dataStore.getStorage().store(uuid, profile);
        assertFalse(result.isError());
    }

    @Test
    @DisplayName("Get updated profile")
    @Order(4)
    public void hasProfileBeenUpdated() {
        final Optional<NickoProfile> profile = dataStore.getData(uuid);
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
        final ActionResult sqlDelete = dataStore.getStorage().delete(uuid);
        assertFalse(sqlDelete.isError());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}