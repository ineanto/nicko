package xyz.atnrch.nicko.test.i18n;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.DataSourceConfiguration;
import xyz.atnrch.nicko.config.DefaultDataSources;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslationTest {
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                DefaultDataSources.SQL_EMPTY,
                DefaultDataSources.REDIS_EMPTY,
                "",
                false);
        MockBukkit.mock();
        MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Translate Line With Replacement")
    public void translateItemTranslationWithoutLore() {
        final I18N i18n = new I18N(Locale.FRENCH);
        final String translation = i18n.translatePrefixless(I18NDict.Event.Settings.ERROR, "Test");
        assertEquals("§cImpossible de mettre à jour vos paramètres. §7§o(Test)", translation);
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
