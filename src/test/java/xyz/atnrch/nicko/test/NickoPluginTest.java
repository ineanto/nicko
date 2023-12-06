package xyz.atnrch.nicko.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DefaultDataSources;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NickoPluginTest {
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                DefaultDataSources.MARIADB_EMPTY,
                DefaultDataSources.REDIS_EMPTY,
                "",
                false);
        MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Plugin Initialization")
    public void initializePlugin() {
        assertNotNull(plugin);
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
