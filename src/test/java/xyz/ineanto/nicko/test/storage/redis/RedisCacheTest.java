package xyz.ineanto.nicko.test.storage.redis;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.*;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DataSourceConfiguration;
import xyz.ineanto.nicko.config.DefaultDataSources;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisCacheTest {
    private static NickoBukkit plugin;
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                DefaultDataSources.SQL_EMPTY,
                new DataSourceConfiguration(true, "127.0.0.1", 6379, "", ""),
                "",
                false);
        final ServerMock server = MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
        player = server.addPlayer();
    }

    @Test
    @DisplayName("Cache Profile")
    @Order(1)
    public void cacheProfile() {
        final Optional<NickoProfile> optionalProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(optionalProfile.isPresent());
        assertTrue(plugin.getDataStore().getCache().isCached(player.getUniqueId()));
    }

    @Test
    @DisplayName("Update Cache Profile")
    @Order(2)
    public void updateCache() {
        final Optional<NickoProfile> optionalProfile = NickoProfile.get(player);
        assertTrue(optionalProfile.isPresent());

        final NickoProfile profile = optionalProfile.get();
        final PlayerDataStore dataStore = plugin.getDataStore();
        profile.setName("Notch");
        dataStore.updateCache(player.getUniqueId(), profile);

        final Optional<NickoProfile> retrieve = dataStore.getCache().retrieve(player.getUniqueId());
        assertTrue(retrieve.isPresent());
        final NickoProfile retrieved = retrieve.get();
        assertEquals(retrieved.getName(), "Notch");
    }

    @Test
    @DisplayName("Delete Cache Profile")
    @Order(3)
    public void deleteCache() {
        final PlayerDataStore dataStore = plugin.getDataStore();
        final ActionResult cacheDelete = dataStore.getCache().delete(player.getUniqueId());
        assertFalse(cacheDelete.isError());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
