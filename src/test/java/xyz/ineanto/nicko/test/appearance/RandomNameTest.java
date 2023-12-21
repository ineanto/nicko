package xyz.ineanto.nicko.test.appearance;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.appearance.random.RandomNameFetcher;
import xyz.ineanto.nicko.config.Configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RandomNameTest {
    private static NickoBukkit plugin;

    @BeforeAll
    public static void setup() {
        final Configuration config = Configuration.DEFAULT;
        MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Get random name")
    public void getRandomName() {
        final RandomNameFetcher randomNameFetcher = new RandomNameFetcher(plugin);
        assertNotNull(randomNameFetcher.getRandomUsername());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
