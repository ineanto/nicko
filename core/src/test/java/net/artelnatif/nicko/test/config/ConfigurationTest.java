package net.artelnatif.nicko.test.config;

import be.seeseemelk.mockbukkit.MockBukkit;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.config.DataSourceConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigurationTest {
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                DataSourceConfiguration.SQL_EMPTY,
                DataSourceConfiguration.REDIS_EMPTY,
                "",
                true,
                false);
        MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Read configuration")
    public void readConfiguration() {
        final Configuration configuration = plugin.getNickoConfig();
        assertTrue(configuration.isLocal());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
