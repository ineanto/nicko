package xyz.ineanto.nicko.test.storage;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.profile.NickoProfile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapCacheTest {
    private static Nicko plugin;
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = Configuration.DEFAULT;
        final ServerMock server = MockBukkit.mock();
        plugin = MockBukkit.load(Nicko.class, config);
        player = server.addPlayer();
    }

    @Test
    @DisplayName("Cache Player Data")
    public void cachePlayerData() {
        final Optional<NickoProfile> optionalProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(optionalProfile.isPresent());
        assertTrue(plugin.getDataStore().getCache().isCached(player.getUniqueId()));
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
