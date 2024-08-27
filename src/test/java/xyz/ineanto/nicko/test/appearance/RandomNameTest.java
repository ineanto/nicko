package xyz.ineanto.nicko.test.appearance;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.random.RandomNameFetcher;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.mojang.MojangUtils;

import static org.junit.jupiter.api.Assertions.*;

public class RandomNameTest {
    private static Nicko plugin;

    @BeforeAll
    public static void setup() {
        final Configuration config = Configuration.DEFAULT;
        MockBukkit.mock();
        plugin = MockBukkit.load(Nicko.class, config);
    }

    @Test
    @DisplayName("Get random name")
    public void getRandomName() {
        final RandomNameFetcher randomNameFetcher = new RandomNameFetcher(plugin);
        final String username = randomNameFetcher.getRandomUsername();
        assertNotNull(username);
        assertFalse(MojangUtils.isUsernameInvalid(username));
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
