package net.artelnatif.nicko.test.cache;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheStorageTest {
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
                false);
        server = MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
        player = server.addPlayer();
    }

    @Test
    @DisplayName("Cache Player Data")
    public void cachePlayerData() {
        final Optional<NickoProfile> optionalProfile = plugin.getDataStore().getData(player.getUniqueId());
        assertTrue(optionalProfile.isPresent());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}