package xyz.atnrch.nicko.test.config;

import be.seeseemelk.mockbukkit.MockBukkit;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.config.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigurationTest {
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class);
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
