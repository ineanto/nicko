package xyz.ineanto.nicko.test.config;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DefaultDataSources;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ConfigurationTest {
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        MockBukkit.mock();
        final Configuration config = new Configuration(
                DefaultDataSources.SQL_EMPTY,
                DefaultDataSources.REDIS_EMPTY,
                "",
                false);
        plugin = MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Read configuration")
    public void readConfiguration() {
        final Configuration configuration = plugin.getNickoConfig();
        assertFalse(configuration.getSqlConfiguration().isEnabled());
        assertFalse(configuration.getRedisConfiguration().isEnabled());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
