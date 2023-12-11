package xyz.ineanto.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.*;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DefaultDataSources;
import xyz.ineanto.nicko.config.SQLDataSourceConfiguration;
import xyz.ineanto.nicko.i18n.Locale;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;

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
                new SQLDataSourceConfiguration(true, "127.0.0.1", 3306, "root", "12345", true),
                DefaultDataSources.REDIS_EMPTY,
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
        final Optional<NickoProfile> optionalProfile = NickoProfile.get(uuid);
        assertTrue(optionalProfile.isPresent());
    }

    @Test
    @DisplayName("Update profile")
    @Order(3)
    public void updateProfile() {
        final Optional<NickoProfile> optionalProfile = NickoProfile.get(uuid);
        assertTrue(optionalProfile.isPresent());

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
        final Optional<NickoProfile> optionalProfile = NickoProfile.get(uuid);
        assertTrue(optionalProfile.isPresent());

        final NickoProfile updatedProfile = optionalProfile.get();
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