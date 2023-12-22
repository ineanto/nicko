package xyz.ineanto.nicko.test.config;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DefaultDataSources;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationVersionTest {
    @BeforeAll
    public static void setup() {
        MockBukkit.mock();
        final Configuration configuration = Configuration.DEFAULT;
        MockBukkit.load(NickoBukkit.class, configuration);
    }

    @Test
    @DisplayName("Compare configuration version")
    public void compareConfigurationVersion() {
        final Configuration configuration = new Configuration(Configuration.VERSION.toString(),
                DefaultDataSources.SQL_EMPTY,
                DefaultDataSources.REDIS_EMPTY,
                "",
                false);
        assertEquals(configuration.getVersionObject().compareTo(Configuration.VERSION), 0);
    }

    @Test
    @DisplayName("Compare newer configuration version")
    public void compareNewerConfigurationVersion() {
        final Configuration configuration = new Configuration("24.1.0",
                DefaultDataSources.SQL_EMPTY,
                DefaultDataSources.REDIS_EMPTY,
                "",
                false);
        assertEquals(configuration.getVersionObject().compareTo(Configuration.VERSION), 1);
    }

    @Test
    @DisplayName("Compare older configuration version")
    public void compareOlderConfigurationVersion() {
        final Configuration configuration = new Configuration("0.23.3",
                DefaultDataSources.SQL_EMPTY,
                DefaultDataSources.REDIS_EMPTY,
                "",
                false);
        assertEquals(configuration.getVersionObject().compareTo(Configuration.VERSION), -1);
    }

    @Test
    @DisplayName("Compare unknown configuration version")
    public void compareUnknownConfigurationVersion() {
        final Configuration configuration = new Configuration(null,
                DefaultDataSources.SQL_EMPTY,
                DefaultDataSources.REDIS_EMPTY,
                "",
                false);
        assertEquals(configuration.getVersionObject().compareTo(Configuration.VERSION), -1);
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
